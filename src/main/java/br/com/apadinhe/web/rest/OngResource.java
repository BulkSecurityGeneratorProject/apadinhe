package br.com.apadinhe.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.apadinhe.domain.Ong;

import br.com.apadinhe.repository.OngRepository;
import br.com.apadinhe.repository.search.OngSearchRepository;
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
 * REST controller for managing Ong.
 */
@RestController
@RequestMapping("/api")
public class OngResource {

    private final Logger log = LoggerFactory.getLogger(OngResource.class);

    private static final String ENTITY_NAME = "ong";

    private final OngRepository ongRepository;

    private final OngSearchRepository ongSearchRepository;

    public OngResource(OngRepository ongRepository, OngSearchRepository ongSearchRepository) {
        this.ongRepository = ongRepository;
        this.ongSearchRepository = ongSearchRepository;
    }

    /**
     * POST  /ongs : Create a new ong.
     *
     * @param ong the ong to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ong, or with status 400 (Bad Request) if the ong has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ongs")
    @Timed
    public ResponseEntity<Ong> createOng(@Valid @RequestBody Ong ong) throws URISyntaxException {
        log.debug("REST request to save Ong : {}", ong);
        if (ong.getId() != null) {
            throw new BadRequestAlertException("A new ong cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ong result = ongRepository.save(ong);
        ongSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ongs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ongs : Updates an existing ong.
     *
     * @param ong the ong to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ong,
     * or with status 400 (Bad Request) if the ong is not valid,
     * or with status 500 (Internal Server Error) if the ong couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ongs")
    @Timed
    public ResponseEntity<Ong> updateOng(@Valid @RequestBody Ong ong) throws URISyntaxException {
        log.debug("REST request to update Ong : {}", ong);
        if (ong.getId() == null) {
            return createOng(ong);
        }
        Ong result = ongRepository.save(ong);
        ongSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ong.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ongs : get all the ongs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ongs in body
     */
    @GetMapping("/ongs")
    @Timed
    public List<Ong> getAllOngs() {
        log.debug("REST request to get all Ongs");
        return ongRepository.findAll();
        }

    /**
     * GET  /ongs/:id : get the "id" ong.
     *
     * @param id the id of the ong to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ong, or with status 404 (Not Found)
     */
    @GetMapping("/ongs/{id}")
    @Timed
    public ResponseEntity<Ong> getOng(@PathVariable Long id) {
        log.debug("REST request to get Ong : {}", id);
        Ong ong = ongRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ong));
    }

    /**
     * DELETE  /ongs/:id : delete the "id" ong.
     *
     * @param id the id of the ong to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ongs/{id}")
    @Timed
    public ResponseEntity<Void> deleteOng(@PathVariable Long id) {
        log.debug("REST request to delete Ong : {}", id);
        ongRepository.delete(id);
        ongSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ongs?query=:query : search for the ong corresponding
     * to the query.
     *
     * @param query the query of the ong search
     * @return the result of the search
     */
    @GetMapping("/_search/ongs")
    @Timed
    public List<Ong> searchOngs(@RequestParam String query) {
        log.debug("REST request to search Ongs for query {}", query);
        return StreamSupport
            .stream(ongSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
