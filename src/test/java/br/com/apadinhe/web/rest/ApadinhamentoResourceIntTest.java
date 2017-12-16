package br.com.apadinhe.web.rest;

import br.com.apadinhe.ApadinheApp;

import br.com.apadinhe.domain.Apadinhamento;
import br.com.apadinhe.domain.User;
import br.com.apadinhe.domain.Crianca;
import br.com.apadinhe.repository.ApadinhamentoRepository;
import br.com.apadinhe.repository.search.ApadinhamentoSearchRepository;
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
 * Test class for the ApadinhamentoResource REST controller.
 *
 * @see ApadinhamentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApadinheApp.class)
public class ApadinhamentoResourceIntTest {

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    @Autowired
    private ApadinhamentoRepository apadinhamentoRepository;

    @Autowired
    private ApadinhamentoSearchRepository apadinhamentoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApadinhamentoMockMvc;

    private Apadinhamento apadinhamento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApadinhamentoResource apadinhamentoResource = new ApadinhamentoResource(apadinhamentoRepository, apadinhamentoSearchRepository);
        this.restApadinhamentoMockMvc = MockMvcBuilders.standaloneSetup(apadinhamentoResource)
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
    public static Apadinhamento createEntity(EntityManager em) {
        Apadinhamento apadinhamento = new Apadinhamento()
            .observacao(DEFAULT_OBSERVACAO);
        // Add required entity
        User padrinho = UserResourceIntTest.createEntity(em);
        em.persist(padrinho);
        em.flush();
        apadinhamento.getPadrinhos().add(padrinho);
        // Add required entity
        Crianca crianca = CriancaResourceIntTest.createEntity(em);
        em.persist(crianca);
        em.flush();
        apadinhamento.getCriancas().add(crianca);
        return apadinhamento;
    }

    @Before
    public void initTest() {
        apadinhamentoSearchRepository.deleteAll();
        apadinhamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createApadinhamento() throws Exception {
        int databaseSizeBeforeCreate = apadinhamentoRepository.findAll().size();

        // Create the Apadinhamento
        restApadinhamentoMockMvc.perform(post("/api/apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apadinhamento)))
            .andExpect(status().isCreated());

        // Validate the Apadinhamento in the database
        List<Apadinhamento> apadinhamentoList = apadinhamentoRepository.findAll();
        assertThat(apadinhamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Apadinhamento testApadinhamento = apadinhamentoList.get(apadinhamentoList.size() - 1);
        assertThat(testApadinhamento.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);

        // Validate the Apadinhamento in Elasticsearch
        Apadinhamento apadinhamentoEs = apadinhamentoSearchRepository.findOne(testApadinhamento.getId());
        assertThat(apadinhamentoEs).isEqualToComparingFieldByField(testApadinhamento);
    }

    @Test
    @Transactional
    public void createApadinhamentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = apadinhamentoRepository.findAll().size();

        // Create the Apadinhamento with an existing ID
        apadinhamento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApadinhamentoMockMvc.perform(post("/api/apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apadinhamento)))
            .andExpect(status().isBadRequest());

        // Validate the Apadinhamento in the database
        List<Apadinhamento> apadinhamentoList = apadinhamentoRepository.findAll();
        assertThat(apadinhamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllApadinhamentos() throws Exception {
        // Initialize the database
        apadinhamentoRepository.saveAndFlush(apadinhamento);

        // Get all the apadinhamentoList
        restApadinhamentoMockMvc.perform(get("/api/apadinhamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apadinhamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())));
    }

    @Test
    @Transactional
    public void getApadinhamento() throws Exception {
        // Initialize the database
        apadinhamentoRepository.saveAndFlush(apadinhamento);

        // Get the apadinhamento
        restApadinhamentoMockMvc.perform(get("/api/apadinhamentos/{id}", apadinhamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(apadinhamento.getId().intValue()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingApadinhamento() throws Exception {
        // Get the apadinhamento
        restApadinhamentoMockMvc.perform(get("/api/apadinhamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApadinhamento() throws Exception {
        // Initialize the database
        apadinhamentoRepository.saveAndFlush(apadinhamento);
        apadinhamentoSearchRepository.save(apadinhamento);
        int databaseSizeBeforeUpdate = apadinhamentoRepository.findAll().size();

        // Update the apadinhamento
        Apadinhamento updatedApadinhamento = apadinhamentoRepository.findOne(apadinhamento.getId());
        updatedApadinhamento
            .observacao(UPDATED_OBSERVACAO);

        restApadinhamentoMockMvc.perform(put("/api/apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedApadinhamento)))
            .andExpect(status().isOk());

        // Validate the Apadinhamento in the database
        List<Apadinhamento> apadinhamentoList = apadinhamentoRepository.findAll();
        assertThat(apadinhamentoList).hasSize(databaseSizeBeforeUpdate);
        Apadinhamento testApadinhamento = apadinhamentoList.get(apadinhamentoList.size() - 1);
        assertThat(testApadinhamento.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);

        // Validate the Apadinhamento in Elasticsearch
        Apadinhamento apadinhamentoEs = apadinhamentoSearchRepository.findOne(testApadinhamento.getId());
        assertThat(apadinhamentoEs).isEqualToComparingFieldByField(testApadinhamento);
    }

    @Test
    @Transactional
    public void updateNonExistingApadinhamento() throws Exception {
        int databaseSizeBeforeUpdate = apadinhamentoRepository.findAll().size();

        // Create the Apadinhamento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApadinhamentoMockMvc.perform(put("/api/apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(apadinhamento)))
            .andExpect(status().isCreated());

        // Validate the Apadinhamento in the database
        List<Apadinhamento> apadinhamentoList = apadinhamentoRepository.findAll();
        assertThat(apadinhamentoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApadinhamento() throws Exception {
        // Initialize the database
        apadinhamentoRepository.saveAndFlush(apadinhamento);
        apadinhamentoSearchRepository.save(apadinhamento);
        int databaseSizeBeforeDelete = apadinhamentoRepository.findAll().size();

        // Get the apadinhamento
        restApadinhamentoMockMvc.perform(delete("/api/apadinhamentos/{id}", apadinhamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean apadinhamentoExistsInEs = apadinhamentoSearchRepository.exists(apadinhamento.getId());
        assertThat(apadinhamentoExistsInEs).isFalse();

        // Validate the database is empty
        List<Apadinhamento> apadinhamentoList = apadinhamentoRepository.findAll();
        assertThat(apadinhamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchApadinhamento() throws Exception {
        // Initialize the database
        apadinhamentoRepository.saveAndFlush(apadinhamento);
        apadinhamentoSearchRepository.save(apadinhamento);

        // Search the apadinhamento
        restApadinhamentoMockMvc.perform(get("/api/_search/apadinhamentos?query=id:" + apadinhamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(apadinhamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Apadinhamento.class);
        Apadinhamento apadinhamento1 = new Apadinhamento();
        apadinhamento1.setId(1L);
        Apadinhamento apadinhamento2 = new Apadinhamento();
        apadinhamento2.setId(apadinhamento1.getId());
        assertThat(apadinhamento1).isEqualTo(apadinhamento2);
        apadinhamento2.setId(2L);
        assertThat(apadinhamento1).isNotEqualTo(apadinhamento2);
        apadinhamento1.setId(null);
        assertThat(apadinhamento1).isNotEqualTo(apadinhamento2);
    }
}
