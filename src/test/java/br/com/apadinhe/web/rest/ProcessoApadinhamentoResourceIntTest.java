package br.com.apadinhe.web.rest;

import br.com.apadinhe.ApadinheApp;

import br.com.apadinhe.domain.ProcessoApadinhamento;
import br.com.apadinhe.domain.User;
import br.com.apadinhe.domain.Crianca;
import br.com.apadinhe.repository.ProcessoApadinhamentoRepository;
import br.com.apadinhe.repository.search.ProcessoApadinhamentoSearchRepository;
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

import br.com.apadinhe.domain.enumeration.ProcessoApadinhamentoSituacao;
/**
 * Test class for the ProcessoApadinhamentoResource REST controller.
 *
 * @see ProcessoApadinhamentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApadinheApp.class)
public class ProcessoApadinhamentoResourceIntTest {

    private static final ProcessoApadinhamentoSituacao DEFAULT_SITUACAO = ProcessoApadinhamentoSituacao.ABERTO;
    private static final ProcessoApadinhamentoSituacao UPDATED_SITUACAO = ProcessoApadinhamentoSituacao.ANALISE;

    private static final String DEFAULT_TEXTO = "AAAAAAAAAA";
    private static final String UPDATED_TEXTO = "BBBBBBBBBB";

    @Autowired
    private ProcessoApadinhamentoRepository processoApadinhamentoRepository;

    @Autowired
    private ProcessoApadinhamentoSearchRepository processoApadinhamentoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcessoApadinhamentoMockMvc;

    private ProcessoApadinhamento processoApadinhamento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcessoApadinhamentoResource processoApadinhamentoResource = new ProcessoApadinhamentoResource(processoApadinhamentoRepository, processoApadinhamentoSearchRepository);
        this.restProcessoApadinhamentoMockMvc = MockMvcBuilders.standaloneSetup(processoApadinhamentoResource)
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
    public static ProcessoApadinhamento createEntity(EntityManager em) {
        ProcessoApadinhamento processoApadinhamento = new ProcessoApadinhamento()
            .situacao(DEFAULT_SITUACAO)
            .texto(DEFAULT_TEXTO);
        // Add required entity
        User padrinho = UserResourceIntTest.createEntity(em);
        em.persist(padrinho);
        em.flush();
        processoApadinhamento.getPadrinhos().add(padrinho);
        // Add required entity
        Crianca crianca = CriancaResourceIntTest.createEntity(em);
        em.persist(crianca);
        em.flush();
        processoApadinhamento.getCriancas().add(crianca);
        return processoApadinhamento;
    }

    @Before
    public void initTest() {
        processoApadinhamentoSearchRepository.deleteAll();
        processoApadinhamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcessoApadinhamento() throws Exception {
        int databaseSizeBeforeCreate = processoApadinhamentoRepository.findAll().size();

        // Create the ProcessoApadinhamento
        restProcessoApadinhamentoMockMvc.perform(post("/api/processo-apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processoApadinhamento)))
            .andExpect(status().isCreated());

        // Validate the ProcessoApadinhamento in the database
        List<ProcessoApadinhamento> processoApadinhamentoList = processoApadinhamentoRepository.findAll();
        assertThat(processoApadinhamentoList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessoApadinhamento testProcessoApadinhamento = processoApadinhamentoList.get(processoApadinhamentoList.size() - 1);
        assertThat(testProcessoApadinhamento.getSituacao()).isEqualTo(DEFAULT_SITUACAO);
        assertThat(testProcessoApadinhamento.getTexto()).isEqualTo(DEFAULT_TEXTO);

        // Validate the ProcessoApadinhamento in Elasticsearch
        ProcessoApadinhamento processoApadinhamentoEs = processoApadinhamentoSearchRepository.findOne(testProcessoApadinhamento.getId());
        assertThat(processoApadinhamentoEs).isEqualToComparingFieldByField(testProcessoApadinhamento);
    }

    @Test
    @Transactional
    public void createProcessoApadinhamentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processoApadinhamentoRepository.findAll().size();

        // Create the ProcessoApadinhamento with an existing ID
        processoApadinhamento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessoApadinhamentoMockMvc.perform(post("/api/processo-apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processoApadinhamento)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessoApadinhamento in the database
        List<ProcessoApadinhamento> processoApadinhamentoList = processoApadinhamentoRepository.findAll();
        assertThat(processoApadinhamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSituacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoApadinhamentoRepository.findAll().size();
        // set the field null
        processoApadinhamento.setSituacao(null);

        // Create the ProcessoApadinhamento, which fails.

        restProcessoApadinhamentoMockMvc.perform(post("/api/processo-apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processoApadinhamento)))
            .andExpect(status().isBadRequest());

        List<ProcessoApadinhamento> processoApadinhamentoList = processoApadinhamentoRepository.findAll();
        assertThat(processoApadinhamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTextoIsRequired() throws Exception {
        int databaseSizeBeforeTest = processoApadinhamentoRepository.findAll().size();
        // set the field null
        processoApadinhamento.setTexto(null);

        // Create the ProcessoApadinhamento, which fails.

        restProcessoApadinhamentoMockMvc.perform(post("/api/processo-apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processoApadinhamento)))
            .andExpect(status().isBadRequest());

        List<ProcessoApadinhamento> processoApadinhamentoList = processoApadinhamentoRepository.findAll();
        assertThat(processoApadinhamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcessoApadinhamentos() throws Exception {
        // Initialize the database
        processoApadinhamentoRepository.saveAndFlush(processoApadinhamento);

        // Get all the processoApadinhamentoList
        restProcessoApadinhamentoMockMvc.perform(get("/api/processo-apadinhamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processoApadinhamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO.toString())));
    }

    @Test
    @Transactional
    public void getProcessoApadinhamento() throws Exception {
        // Initialize the database
        processoApadinhamentoRepository.saveAndFlush(processoApadinhamento);

        // Get the processoApadinhamento
        restProcessoApadinhamentoMockMvc.perform(get("/api/processo-apadinhamentos/{id}", processoApadinhamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(processoApadinhamento.getId().intValue()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.texto").value(DEFAULT_TEXTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProcessoApadinhamento() throws Exception {
        // Get the processoApadinhamento
        restProcessoApadinhamentoMockMvc.perform(get("/api/processo-apadinhamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcessoApadinhamento() throws Exception {
        // Initialize the database
        processoApadinhamentoRepository.saveAndFlush(processoApadinhamento);
        processoApadinhamentoSearchRepository.save(processoApadinhamento);
        int databaseSizeBeforeUpdate = processoApadinhamentoRepository.findAll().size();

        // Update the processoApadinhamento
        ProcessoApadinhamento updatedProcessoApadinhamento = processoApadinhamentoRepository.findOne(processoApadinhamento.getId());
        updatedProcessoApadinhamento
            .situacao(UPDATED_SITUACAO)
            .texto(UPDATED_TEXTO);

        restProcessoApadinhamentoMockMvc.perform(put("/api/processo-apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcessoApadinhamento)))
            .andExpect(status().isOk());

        // Validate the ProcessoApadinhamento in the database
        List<ProcessoApadinhamento> processoApadinhamentoList = processoApadinhamentoRepository.findAll();
        assertThat(processoApadinhamentoList).hasSize(databaseSizeBeforeUpdate);
        ProcessoApadinhamento testProcessoApadinhamento = processoApadinhamentoList.get(processoApadinhamentoList.size() - 1);
        assertThat(testProcessoApadinhamento.getSituacao()).isEqualTo(UPDATED_SITUACAO);
        assertThat(testProcessoApadinhamento.getTexto()).isEqualTo(UPDATED_TEXTO);

        // Validate the ProcessoApadinhamento in Elasticsearch
        ProcessoApadinhamento processoApadinhamentoEs = processoApadinhamentoSearchRepository.findOne(testProcessoApadinhamento.getId());
        assertThat(processoApadinhamentoEs).isEqualToComparingFieldByField(testProcessoApadinhamento);
    }

    @Test
    @Transactional
    public void updateNonExistingProcessoApadinhamento() throws Exception {
        int databaseSizeBeforeUpdate = processoApadinhamentoRepository.findAll().size();

        // Create the ProcessoApadinhamento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcessoApadinhamentoMockMvc.perform(put("/api/processo-apadinhamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processoApadinhamento)))
            .andExpect(status().isCreated());

        // Validate the ProcessoApadinhamento in the database
        List<ProcessoApadinhamento> processoApadinhamentoList = processoApadinhamentoRepository.findAll();
        assertThat(processoApadinhamentoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcessoApadinhamento() throws Exception {
        // Initialize the database
        processoApadinhamentoRepository.saveAndFlush(processoApadinhamento);
        processoApadinhamentoSearchRepository.save(processoApadinhamento);
        int databaseSizeBeforeDelete = processoApadinhamentoRepository.findAll().size();

        // Get the processoApadinhamento
        restProcessoApadinhamentoMockMvc.perform(delete("/api/processo-apadinhamentos/{id}", processoApadinhamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean processoApadinhamentoExistsInEs = processoApadinhamentoSearchRepository.exists(processoApadinhamento.getId());
        assertThat(processoApadinhamentoExistsInEs).isFalse();

        // Validate the database is empty
        List<ProcessoApadinhamento> processoApadinhamentoList = processoApadinhamentoRepository.findAll();
        assertThat(processoApadinhamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProcessoApadinhamento() throws Exception {
        // Initialize the database
        processoApadinhamentoRepository.saveAndFlush(processoApadinhamento);
        processoApadinhamentoSearchRepository.save(processoApadinhamento);

        // Search the processoApadinhamento
        restProcessoApadinhamentoMockMvc.perform(get("/api/_search/processo-apadinhamentos?query=id:" + processoApadinhamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processoApadinhamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].texto").value(hasItem(DEFAULT_TEXTO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessoApadinhamento.class);
        ProcessoApadinhamento processoApadinhamento1 = new ProcessoApadinhamento();
        processoApadinhamento1.setId(1L);
        ProcessoApadinhamento processoApadinhamento2 = new ProcessoApadinhamento();
        processoApadinhamento2.setId(processoApadinhamento1.getId());
        assertThat(processoApadinhamento1).isEqualTo(processoApadinhamento2);
        processoApadinhamento2.setId(2L);
        assertThat(processoApadinhamento1).isNotEqualTo(processoApadinhamento2);
        processoApadinhamento1.setId(null);
        assertThat(processoApadinhamento1).isNotEqualTo(processoApadinhamento2);
    }
}
