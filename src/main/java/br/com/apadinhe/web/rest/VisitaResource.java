package br.com.apadinhe.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.apadinhe.domain.Visita;

import br.com.apadinhe.repository.VisitaRepository;
import br.com.apadinhe.repository.search.VisitaSearchRepository;
import br.com.apadinhe.web.rest.errors.BadRequestAlertException;
import br.com.apadinhe.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Visita.
 */
@RestController
@RequestMapping("/api")
public class VisitaResource {

    private final Logger log = LoggerFactory.getLogger(VisitaResource.class);

    private static final String ENTITY_NAME = "visita";

    private final VisitaRepository visitaRepository;

    private final VisitaSearchRepository visitaSearchRepository;

    public VisitaResource(VisitaRepository visitaRepository, VisitaSearchRepository visitaSearchRepository) {
        this.visitaRepository = visitaRepository;
        this.visitaSearchRepository = visitaSearchRepository;
    }

    /**
     * POST  /visitas : Create a new visita.
     *
     * @param visita the visita to create
     * @return the ResponseEntity with status 201 (Created) and with body the new visita, or with status 400 (Bad Request) if the visita has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/visitas")
    @Timed
    public ResponseEntity<Visita> createVisita(@Valid @RequestBody Visita visita) throws URISyntaxException {
        log.debug("REST request to save Visita : {}", visita);
        if (visita.getId() != null) {
            throw new BadRequestAlertException("A new visita cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Visita result = visitaRepository.save(visita);
        visitaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/visitas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /visitas : Updates an existing visita.
     *
     * @param visita the visita to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated visita,
     * or with status 400 (Bad Request) if the visita is not valid,
     * or with status 500 (Internal Server Error) if the visita couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/visitas")
    @Timed
    public ResponseEntity<Visita> updateVisita(@Valid @RequestBody Visita visita) throws URISyntaxException {
        log.debug("REST request to update Visita : {}", visita);
        if (visita.getId() == null) {
            return createVisita(visita);
        }
        Visita result = visitaRepository.save(visita);
        visitaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, visita.getId().toString()))
            .body(result);
    }

    /**
     * GET  /visitas : get all the visitas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of visitas in body
     */
    @GetMapping("/visitas")
    @Timed
    public List<Visita> getAllVisitas() {
        log.debug("REST request to get all Visitas");
        return visitaRepository.findAll();
        }

    /**
     * GET  /visitas/:id : get the "id" visita.
     *
     * @param id the id of the visita to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the visita, or with status 404 (Not Found)
     */
    @GetMapping("/visitas/{id}")
    @Timed
    public ResponseEntity<Visita> getVisita(@PathVariable Long id) {
        log.debug("REST request to get Visita : {}", id);
        Visita visita = visitaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(visita));
    }

    /**
     * DELETE  /visitas/:id : delete the "id" visita.
     *
     * @param id the id of the visita to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/visitas/{id}")
    @Timed
    public ResponseEntity<Void> deleteVisita(@PathVariable Long id) {
        log.debug("REST request to delete Visita : {}", id);
        visitaRepository.delete(id);
        visitaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/visitas?query=:query : search for the visita corresponding
     * to the query.
     *
     * @param query the query of the visita search
     * @return the result of the search
     */
    @GetMapping("/_search/visitas")
    @Timed
    public List<Visita> searchVisitas(@RequestParam String query) {
        log.debug("REST request to search Visitas for query {}", query);
        return StreamSupport
            .stream(visitaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
