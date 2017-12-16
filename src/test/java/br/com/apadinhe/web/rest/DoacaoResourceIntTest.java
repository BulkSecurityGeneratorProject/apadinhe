package br.com.apadinhe.web.rest;

import br.com.apadinhe.ApadinheApp;

import br.com.apadinhe.domain.Doacao;
import br.com.apadinhe.domain.Ong;
import br.com.apadinhe.domain.User;
import br.com.apadinhe.repository.DoacaoRepository;
import br.com.apadinhe.repository.search.DoacaoSearchRepository;
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
import java.util.List;

import static br.com.apadinhe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DoacaoResource REST controller.
 *
 * @see DoacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApadinheApp.class)
public class DoacaoResourceIntTest {

    private static final String DEFAULT_COMPROVANTE = "AAAAAAAAAA";
    private static final String UPDATED_COMPROVANTE = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private DoacaoSearchRepository doacaoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDoacaoMockMvc;

    private Doacao doacao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DoacaoResource doacaoResource = new DoacaoResource(doacaoRepository, doacaoSearchRepository);
        this.restDoacaoMockMvc = MockMvcBuilders.standaloneSetup(doacaoResource)
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
    public static Doacao createEntity(EntityManager em) {
        Doacao doacao = new Doacao()
            .comprovante(DEFAULT_COMPROVANTE)
            .valor(DEFAULT_VALOR);
        // Add required entity
        Ong ong = OngResourceIntTest.createEntity(em);
        em.persist(ong);
        em.flush();
        doacao.setOng(ong);
        // Add required entity
        User doador = UserResourceIntTest.createEntity(em);
        em.persist(doador);
        em.flush();
        doacao.setDoador(doador);
        return doacao;
    }

    @Before
    public void initTest() {
        doacaoSearchRepository.deleteAll();
        doacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoacao() throws Exception {
        int databaseSizeBeforeCreate = doacaoRepository.findAll().size();

        // Create the Doacao
        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isCreated());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Doacao testDoacao = doacaoList.get(doacaoList.size() - 1);
        assertThat(testDoacao.getComprovante()).isEqualTo(DEFAULT_COMPROVANTE);
        assertThat(testDoacao.getValor()).isEqualTo(DEFAULT_VALOR);

        // Validate the Doacao in Elasticsearch
        Doacao doacaoEs = doacaoSearchRepository.findOne(testDoacao.getId());
        assertThat(doacaoEs).isEqualToComparingFieldByField(testDoacao);
    }

    @Test
    @Transactional
    public void createDoacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doacaoRepository.findAll().size();

        // Create the Doacao with an existing ID
        doacao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = doacaoRepository.findAll().size();
        // set the field null
        doacao.setValor(null);

        // Create the Doacao, which fails.

        restDoacaoMockMvc.perform(post("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDoacaos() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        // Get all the doacaoList
        restDoacaoMockMvc.perform(get("/api/doacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].comprovante").value(hasItem(DEFAULT_COMPROVANTE.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())));
    }

    @Test
    @Transactional
    public void getDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        // Get the doacao
        restDoacaoMockMvc.perform(get("/api/doacaos/{id}", doacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(doacao.getId().intValue()))
            .andExpect(jsonPath("$.comprovante").value(DEFAULT_COMPROVANTE.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDoacao() throws Exception {
        // Get the doacao
        restDoacaoMockMvc.perform(get("/api/doacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);
        doacaoSearchRepository.save(doacao);
        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();

        // Update the doacao
        Doacao updatedDoacao = doacaoRepository.findOne(doacao.getId());
        updatedDoacao
            .comprovante(UPDATED_COMPROVANTE)
            .valor(UPDATED_VALOR);

        restDoacaoMockMvc.perform(put("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDoacao)))
            .andExpect(status().isOk());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
        Doacao testDoacao = doacaoList.get(doacaoList.size() - 1);
        assertThat(testDoacao.getComprovante()).isEqualTo(UPDATED_COMPROVANTE);
        assertThat(testDoacao.getValor()).isEqualTo(UPDATED_VALOR);

        // Validate the Doacao in Elasticsearch
        Doacao doacaoEs = doacaoSearchRepository.findOne(testDoacao.getId());
        assertThat(doacaoEs).isEqualToComparingFieldByField(testDoacao);
    }

    @Test
    @Transactional
    public void updateNonExistingDoacao() throws Exception {
        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();

        // Create the Doacao

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDoacaoMockMvc.perform(put("/api/doacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isCreated());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);
        doacaoSearchRepository.save(doacao);
        int databaseSizeBeforeDelete = doacaoRepository.findAll().size();

        // Get the doacao
        restDoacaoMockMvc.perform(delete("/api/doacaos/{id}", doacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean doacaoExistsInEs = doacaoSearchRepository.exists(doacao.getId());
        assertThat(doacaoExistsInEs).isFalse();

        // Validate the database is empty
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);
        doacaoSearchRepository.save(doacao);

        // Search the doacao
        restDoacaoMockMvc.perform(get("/api/_search/doacaos?query=id:" + doacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].comprovante").value(hasItem(DEFAULT_COMPROVANTE.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doacao.class);
        Doacao doacao1 = new Doacao();
        doacao1.setId(1L);
        Doacao doacao2 = new Doacao();
        doacao2.setId(doacao1.getId());
        assertThat(doacao1).isEqualTo(doacao2);
        doacao2.setId(2L);
        assertThat(doacao1).isNotEqualTo(doacao2);
        doacao1.setId(null);
        assertThat(doacao1).isNotEqualTo(doacao2);
    }
}
