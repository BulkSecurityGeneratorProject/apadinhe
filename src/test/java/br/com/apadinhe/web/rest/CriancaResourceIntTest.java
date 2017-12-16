package br.com.apadinhe.web.rest;

import br.com.apadinhe.ApadinheApp;

import br.com.apadinhe.domain.Crianca;
import br.com.apadinhe.domain.Ong;
import br.com.apadinhe.repository.CriancaRepository;
import br.com.apadinhe.repository.search.CriancaSearchRepository;
import br.com.apadinhe.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static br.com.apadinhe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CriancaResource REST controller.
 *
 * @see CriancaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApadinheApp.class)
public class CriancaResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PREFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_PREFERENCIA = "BBBBBBBBBB";

    @Autowired
    private CriancaRepository criancaRepository;

    @Autowired
    private CriancaSearchRepository criancaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCriancaMockMvc;

    private Crianca crianca;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CriancaResource criancaResource = new CriancaResource(criancaRepository, criancaSearchRepository);
        this.restCriancaMockMvc = MockMvcBuilders.standaloneSetup(criancaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crianca createEntity(EntityManager em) {
        Crianca crianca = new Crianca()
            .nome(DEFAULT_NOME)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .preferencia(DEFAULT_PREFERENCIA);
        // Add required entity
        Ong ong = OngResourceIntTest.createEntity(em);
        em.persist(ong);
        em.flush();
        crianca.setOng(ong);
        return crianca;
    }

    @Before
    public void initTest() {
        criancaSearchRepository.deleteAll();
        crianca = createEntity(em);
    }

    @Test
    @Transactional
    public void createCrianca() throws Exception {
        int databaseSizeBeforeCreate = criancaRepository.findAll().size();

        // Create the Crianca
        restCriancaMockMvc.perform(post("/api/criancas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crianca)))
            .andExpect(status().isCreated());

        // Validate the Crianca in the database
        List<Crianca> criancaList = criancaRepository.findAll();
        assertThat(criancaList).hasSize(databaseSizeBeforeCreate + 1);
        Crianca testCrianca = criancaList.get(criancaList.size() - 1);
        assertThat(testCrianca.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCrianca.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testCrianca.getPreferencia()).isEqualTo(DEFAULT_PREFERENCIA);

        // Validate the Crianca in Elasticsearch
        Crianca criancaEs = criancaSearchRepository.findOne(testCrianca.getId());
        assertThat(criancaEs).isEqualToComparingFieldByField(testCrianca);
    }

    @Test
    @Transactional
    public void createCriancaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = criancaRepository.findAll().size();

        // Create the Crianca with an existing ID
        crianca.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCriancaMockMvc.perform(post("/api/criancas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crianca)))
            .andExpect(status().isBadRequest());

        // Validate the Crianca in the database
        List<Crianca> criancaList = criancaRepository.findAll();
        assertThat(criancaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = criancaRepository.findAll().size();
        // set the field null
        crianca.setNome(null);

        // Create the Crianca, which fails.

        restCriancaMockMvc.perform(post("/api/criancas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crianca)))
            .andExpect(status().isBadRequest());

        List<Crianca> criancaList = criancaRepository.findAll();
        assertThat(criancaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataNascimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = criancaRepository.findAll().size();
        // set the field null
        crianca.setDataNascimento(null);

        // Create the Crianca, which fails.

        restCriancaMockMvc.perform(post("/api/criancas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crianca)))
            .andExpect(status().isBadRequest());

        List<Crianca> criancaList = criancaRepository.findAll();
        assertThat(criancaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPreferenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = criancaRepository.findAll().size();
        // set the field null
        crianca.setPreferencia(null);

        // Create the Crianca, which fails.

        restCriancaMockMvc.perform(post("/api/criancas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crianca)))
            .andExpect(status().isBadRequest());

        List<Crianca> criancaList = criancaRepository.findAll();
        assertThat(criancaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCriancas() throws Exception {
        // Initialize the database
        criancaRepository.saveAndFlush(crianca);

        // Get all the criancaList
        restCriancaMockMvc.perform(get("/api/criancas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crianca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].preferencia").value(hasItem(DEFAULT_PREFERENCIA.toString())));
    }

    @Test
    @Transactional
    public void getCrianca() throws Exception {
        // Initialize the database
        criancaRepository.saveAndFlush(crianca);

        // Get the crianca
        restCriancaMockMvc.perform(get("/api/criancas/{id}", crianca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(crianca.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.preferencia").value(DEFAULT_PREFERENCIA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCrianca() throws Exception {
        // Get the crianca
        restCriancaMockMvc.perform(get("/api/criancas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCrianca() throws Exception {
        // Initialize the database
        criancaRepository.saveAndFlush(crianca);
        criancaSearchRepository.save(crianca);
        int databaseSizeBeforeUpdate = criancaRepository.findAll().size();

        // Update the crianca
        Crianca updatedCrianca = criancaRepository.findOne(crianca.getId());
        updatedCrianca
            .nome(UPDATED_NOME)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .preferencia(UPDATED_PREFERENCIA);

        restCriancaMockMvc.perform(put("/api/criancas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCrianca)))
            .andExpect(status().isOk());

        // Validate the Crianca in the database
        List<Crianca> criancaList = criancaRepository.findAll();
        assertThat(criancaList).hasSize(databaseSizeBeforeUpdate);
        Crianca testCrianca = criancaList.get(criancaList.size() - 1);
        assertThat(testCrianca.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCrianca.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testCrianca.getPreferencia()).isEqualTo(UPDATED_PREFERENCIA);

        // Validate the Crianca in Elasticsearch
        Crianca criancaEs = criancaSearchRepository.findOne(testCrianca.getId());
        assertThat(criancaEs).isEqualToComparingFieldByField(testCrianca);
    }

    @Test
    @Transactional
    public void updateNonExistingCrianca() throws Exception {
        int databaseSizeBeforeUpdate = criancaRepository.findAll().size();

        // Create the Crianca

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCriancaMockMvc.perform(put("/api/criancas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crianca)))
            .andExpect(status().isCreated());

        // Validate the Crianca in the database
        List<Crianca> criancaList = criancaRepository.findAll();
        assertThat(criancaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCrianca() throws Exception {
        // Initialize the database
        criancaRepository.saveAndFlush(crianca);
        criancaSearchRepository.save(crianca);
        int databaseSizeBeforeDelete = criancaRepository.findAll().size();

        // Get the crianca
        restCriancaMockMvc.perform(delete("/api/criancas/{id}", crianca.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean criancaExistsInEs = criancaSearchRepository.exists(crianca.getId());
        assertThat(criancaExistsInEs).isFalse();

        // Validate the database is empty
        List<Crianca> criancaList = criancaRepository.findAll();
        assertThat(criancaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCrianca() throws Exception {
        // Initialize the database
        criancaRepository.saveAndFlush(crianca);
        criancaSearchRepository.save(crianca);

        // Search the crianca
        restCriancaMockMvc.perform(get("/api/_search/criancas?query=id:" + crianca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crianca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].preferencia").value(hasItem(DEFAULT_PREFERENCIA.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crianca.class);
        Crianca crianca1 = new Crianca();
        crianca1.setId(1L);
        Crianca crianca2 = new Crianca();
        crianca2.setId(crianca1.getId());
        assertThat(crianca1).isEqualTo(crianca2);
        crianca2.setId(2L);
        assertThat(crianca1).isNotEqualTo(crianca2);
        crianca1.setId(null);
        assertThat(crianca1).isNotEqualTo(crianca2);
    }
}
