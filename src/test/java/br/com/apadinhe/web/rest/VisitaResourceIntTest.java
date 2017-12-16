package br.com.apadinhe.web.rest;

import br.com.apadinhe.ApadinheApp;

import br.com.apadinhe.domain.Visita;
import br.com.apadinhe.domain.Crianca;
import br.com.apadinhe.domain.User;
import br.com.apadinhe.repository.VisitaRepository;
import br.com.apadinhe.repository.search.VisitaSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static br.com.apadinhe.web.rest.TestUtil.sameInstant;
import static br.com.apadinhe.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VisitaResource REST controller.
 *
 * @see VisitaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApadinheApp.class)
public class VisitaResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private VisitaSearchRepository visitaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVisitaMockMvc;

    private Visita visita;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VisitaResource visitaResource = new VisitaResource(visitaRepository, visitaSearchRepository);
        this.restVisitaMockMvc = MockMvcBuilders.standaloneSetup(visitaResource)
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
    public static Visita createEntity(EntityManager em) {
        Visita visita = new Visita()
            .data(DEFAULT_DATA);
        // Add required entity
        Crianca crianca = CriancaResourceIntTest.createEntity(em);
        em.persist(crianca);
        em.flush();
        visita.setCrianca(crianca);
        // Add required entity
        User padrinho = UserResourceIntTest.createEntity(em);
        em.persist(padrinho);
        em.flush();
        visita.setPadrinho(padrinho);
        return visita;
    }

    @Before
    public void initTest() {
        visitaSearchRepository.deleteAll();
        visita = createEntity(em);
    }

    @Test
    @Transactional
    public void createVisita() throws Exception {
        int databaseSizeBeforeCreate = visitaRepository.findAll().size();

        // Create the Visita
        restVisitaMockMvc.perform(post("/api/visitas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visita)))
            .andExpect(status().isCreated());

        // Validate the Visita in the database
        List<Visita> visitaList = visitaRepository.findAll();
        assertThat(visitaList).hasSize(databaseSizeBeforeCreate + 1);
        Visita testVisita = visitaList.get(visitaList.size() - 1);
        assertThat(testVisita.getData()).isEqualTo(DEFAULT_DATA);

        // Validate the Visita in Elasticsearch
        Visita visitaEs = visitaSearchRepository.findOne(testVisita.getId());
        assertThat(visitaEs).isEqualToComparingFieldByField(testVisita);
    }

    @Test
    @Transactional
    public void createVisitaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = visitaRepository.findAll().size();

        // Create the Visita with an existing ID
        visita.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVisitaMockMvc.perform(post("/api/visitas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visita)))
            .andExpect(status().isBadRequest());

        // Validate the Visita in the database
        List<Visita> visitaList = visitaRepository.findAll();
        assertThat(visitaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = visitaRepository.findAll().size();
        // set the field null
        visita.setData(null);

        // Create the Visita, which fails.

        restVisitaMockMvc.perform(post("/api/visitas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visita)))
            .andExpect(status().isBadRequest());

        List<Visita> visitaList = visitaRepository.findAll();
        assertThat(visitaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVisitas() throws Exception {
        // Initialize the database
        visitaRepository.saveAndFlush(visita);

        // Get all the visitaList
        restVisitaMockMvc.perform(get("/api/visitas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visita.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))));
    }

    @Test
    @Transactional
    public void getVisita() throws Exception {
        // Initialize the database
        visitaRepository.saveAndFlush(visita);

        // Get the visita
        restVisitaMockMvc.perform(get("/api/visitas/{id}", visita.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(visita.getId().intValue()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)));
    }

    @Test
    @Transactional
    public void getNonExistingVisita() throws Exception {
        // Get the visita
        restVisitaMockMvc.perform(get("/api/visitas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVisita() throws Exception {
        // Initialize the database
        visitaRepository.saveAndFlush(visita);
        visitaSearchRepository.save(visita);
        int databaseSizeBeforeUpdate = visitaRepository.findAll().size();

        // Update the visita
        Visita updatedVisita = visitaRepository.findOne(visita.getId());
        updatedVisita
            .data(UPDATED_DATA);

        restVisitaMockMvc.perform(put("/api/visitas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVisita)))
            .andExpect(status().isOk());

        // Validate the Visita in the database
        List<Visita> visitaList = visitaRepository.findAll();
        assertThat(visitaList).hasSize(databaseSizeBeforeUpdate);
        Visita testVisita = visitaList.get(visitaList.size() - 1);
        assertThat(testVisita.getData()).isEqualTo(UPDATED_DATA);

        // Validate the Visita in Elasticsearch
        Visita visitaEs = visitaSearchRepository.findOne(testVisita.getId());
        assertThat(visitaEs).isEqualToComparingFieldByField(testVisita);
    }

    @Test
    @Transactional
    public void updateNonExistingVisita() throws Exception {
        int databaseSizeBeforeUpdate = visitaRepository.findAll().size();

        // Create the Visita

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVisitaMockMvc.perform(put("/api/visitas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(visita)))
            .andExpect(status().isCreated());

        // Validate the Visita in the database
        List<Visita> visitaList = visitaRepository.findAll();
        assertThat(visitaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVisita() throws Exception {
        // Initialize the database
        visitaRepository.saveAndFlush(visita);
        visitaSearchRepository.save(visita);
        int databaseSizeBeforeDelete = visitaRepository.findAll().size();

        // Get the visita
        restVisitaMockMvc.perform(delete("/api/visitas/{id}", visita.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean visitaExistsInEs = visitaSearchRepository.exists(visita.getId());
        assertThat(visitaExistsInEs).isFalse();

        // Validate the database is empty
        List<Visita> visitaList = visitaRepository.findAll();
        assertThat(visitaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVisita() throws Exception {
        // Initialize the database
        visitaRepository.saveAndFlush(visita);
        visitaSearchRepository.save(visita);

        // Search the visita
        restVisitaMockMvc.perform(get("/api/_search/visitas?query=id:" + visita.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(visita.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Visita.class);
        Visita visita1 = new Visita();
        visita1.setId(1L);
        Visita visita2 = new Visita();
        visita2.setId(visita1.getId());
        assertThat(visita1).isEqualTo(visita2);
        visita2.setId(2L);
        assertThat(visita1).isNotEqualTo(visita2);
        visita1.setId(null);
        assertThat(visita1).isNotEqualTo(visita2);
    }
}
