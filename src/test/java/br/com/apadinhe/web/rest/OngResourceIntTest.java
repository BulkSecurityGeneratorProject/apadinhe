package br.com.apadinhe.web.rest;

import br.com.apadinhe.ApadinheApp;

import br.com.apadinhe.domain.Ong;
import br.com.apadinhe.repository.OngRepository;
import br.com.apadinhe.repository.search.OngSearchRepository;
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

import br.com.apadinhe.domain.enumeration.Banco;
/**
 * Test class for the OngResource REST controller.
 *
 * @see OngResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApadinheApp.class)
public class OngResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_REQUISITOS_APADRINHAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_REQUISITOS_APADRINHAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_CONTA = "AAAAAAAAAA";
    private static final String UPDATED_CONTA = "BBBBBBBBBB";

    private static final String DEFAULT_AGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_AGENCIA = "BBBBBBBBBB";

    private static final Banco DEFAULT_BANCO = Banco.BB;
    private static final Banco UPDATED_BANCO = Banco.CAIXA;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    @Autowired
    private OngRepository ongRepository;

    @Autowired
    private OngSearchRepository ongSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOngMockMvc;

    private Ong ong;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OngResource ongResource = new OngResource(ongRepository, ongSearchRepository);
        this.restOngMockMvc = MockMvcBuilders.standaloneSetup(ongResource)
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
    public static Ong createEntity(EntityManager em) {
        Ong ong = new Ong()
            .nome(DEFAULT_NOME)
            .requisitosApadrinhamento(DEFAULT_REQUISITOS_APADRINHAMENTO)
            .conta(DEFAULT_CONTA)
            .agencia(DEFAULT_AGENCIA)
            .banco(DEFAULT_BANCO)
            .email(DEFAULT_EMAIL)
            .telefone(DEFAULT_TELEFONE);
        return ong;
    }

    @Before
    public void initTest() {
        ongSearchRepository.deleteAll();
        ong = createEntity(em);
    }

    @Test
    @Transactional
    public void createOng() throws Exception {
        int databaseSizeBeforeCreate = ongRepository.findAll().size();

        // Create the Ong
        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isCreated());

        // Validate the Ong in the database
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeCreate + 1);
        Ong testOng = ongList.get(ongList.size() - 1);
        assertThat(testOng.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testOng.getRequisitosApadrinhamento()).isEqualTo(DEFAULT_REQUISITOS_APADRINHAMENTO);
        assertThat(testOng.getConta()).isEqualTo(DEFAULT_CONTA);
        assertThat(testOng.getAgencia()).isEqualTo(DEFAULT_AGENCIA);
        assertThat(testOng.getBanco()).isEqualTo(DEFAULT_BANCO);
        assertThat(testOng.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOng.getTelefone()).isEqualTo(DEFAULT_TELEFONE);

        // Validate the Ong in Elasticsearch
        Ong ongEs = ongSearchRepository.findOne(testOng.getId());
        assertThat(ongEs).isEqualToComparingFieldByField(testOng);
    }

    @Test
    @Transactional
    public void createOngWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ongRepository.findAll().size();

        // Create the Ong with an existing ID
        ong.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        // Validate the Ong in the database
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRepository.findAll().size();
        // set the field null
        ong.setNome(null);

        // Create the Ong, which fails.

        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRepository.findAll().size();
        // set the field null
        ong.setConta(null);

        // Create the Ong, which fails.

        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgenciaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRepository.findAll().size();
        // set the field null
        ong.setAgencia(null);

        // Create the Ong, which fails.

        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBancoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRepository.findAll().size();
        // set the field null
        ong.setBanco(null);

        // Create the Ong, which fails.

        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRepository.findAll().size();
        // set the field null
        ong.setEmail(null);

        // Create the Ong, which fails.

        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelefoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRepository.findAll().size();
        // set the field null
        ong.setTelefone(null);

        // Create the Ong, which fails.

        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOngs() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList
        restOngMockMvc.perform(get("/api/ongs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ong.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].requisitosApadrinhamento").value(hasItem(DEFAULT_REQUISITOS_APADRINHAMENTO.toString())))
            .andExpect(jsonPath("$.[*].conta").value(hasItem(DEFAULT_CONTA.toString())))
            .andExpect(jsonPath("$.[*].agencia").value(hasItem(DEFAULT_AGENCIA.toString())))
            .andExpect(jsonPath("$.[*].banco").value(hasItem(DEFAULT_BANCO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE.toString())));
    }

    @Test
    @Transactional
    public void getOng() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get the ong
        restOngMockMvc.perform(get("/api/ongs/{id}", ong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ong.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.requisitosApadrinhamento").value(DEFAULT_REQUISITOS_APADRINHAMENTO.toString()))
            .andExpect(jsonPath("$.conta").value(DEFAULT_CONTA.toString()))
            .andExpect(jsonPath("$.agencia").value(DEFAULT_AGENCIA.toString()))
            .andExpect(jsonPath("$.banco").value(DEFAULT_BANCO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOng() throws Exception {
        // Get the ong
        restOngMockMvc.perform(get("/api/ongs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOng() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);
        ongSearchRepository.save(ong);
        int databaseSizeBeforeUpdate = ongRepository.findAll().size();

        // Update the ong
        Ong updatedOng = ongRepository.findOne(ong.getId());
        updatedOng
            .nome(UPDATED_NOME)
            .requisitosApadrinhamento(UPDATED_REQUISITOS_APADRINHAMENTO)
            .conta(UPDATED_CONTA)
            .agencia(UPDATED_AGENCIA)
            .banco(UPDATED_BANCO)
            .email(UPDATED_EMAIL)
            .telefone(UPDATED_TELEFONE);

        restOngMockMvc.perform(put("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOng)))
            .andExpect(status().isOk());

        // Validate the Ong in the database
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeUpdate);
        Ong testOng = ongList.get(ongList.size() - 1);
        assertThat(testOng.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testOng.getRequisitosApadrinhamento()).isEqualTo(UPDATED_REQUISITOS_APADRINHAMENTO);
        assertThat(testOng.getConta()).isEqualTo(UPDATED_CONTA);
        assertThat(testOng.getAgencia()).isEqualTo(UPDATED_AGENCIA);
        assertThat(testOng.getBanco()).isEqualTo(UPDATED_BANCO);
        assertThat(testOng.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOng.getTelefone()).isEqualTo(UPDATED_TELEFONE);

        // Validate the Ong in Elasticsearch
        Ong ongEs = ongSearchRepository.findOne(testOng.getId());
        assertThat(ongEs).isEqualToComparingFieldByField(testOng);
    }

    @Test
    @Transactional
    public void updateNonExistingOng() throws Exception {
        int databaseSizeBeforeUpdate = ongRepository.findAll().size();

        // Create the Ong

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOngMockMvc.perform(put("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isCreated());

        // Validate the Ong in the database
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOng() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);
        ongSearchRepository.save(ong);
        int databaseSizeBeforeDelete = ongRepository.findAll().size();

        // Get the ong
        restOngMockMvc.perform(delete("/api/ongs/{id}", ong.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean ongExistsInEs = ongSearchRepository.exists(ong.getId());
        assertThat(ongExistsInEs).isFalse();

        // Validate the database is empty
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOng() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);
        ongSearchRepository.save(ong);

        // Search the ong
        restOngMockMvc.perform(get("/api/_search/ongs?query=id:" + ong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ong.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].requisitosApadrinhamento").value(hasItem(DEFAULT_REQUISITOS_APADRINHAMENTO.toString())))
            .andExpect(jsonPath("$.[*].conta").value(hasItem(DEFAULT_CONTA.toString())))
            .andExpect(jsonPath("$.[*].agencia").value(hasItem(DEFAULT_AGENCIA.toString())))
            .andExpect(jsonPath("$.[*].banco").value(hasItem(DEFAULT_BANCO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ong.class);
        Ong ong1 = new Ong();
        ong1.setId(1L);
        Ong ong2 = new Ong();
        ong2.setId(ong1.getId());
        assertThat(ong1).isEqualTo(ong2);
        ong2.setId(2L);
        assertThat(ong1).isNotEqualTo(ong2);
        ong1.setId(null);
        assertThat(ong1).isNotEqualTo(ong2);
    }
}
