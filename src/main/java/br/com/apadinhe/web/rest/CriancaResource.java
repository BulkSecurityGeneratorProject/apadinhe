package br.com.apadinhe.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.apadinhe.domain.Crianca;

import br.com.apadinhe.repository.CriancaRepository;
import br.com.apadinhe.repository.search.CriancaSearchRepository;
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
 * REST controller for managing Crianca.
 */
@RestController
@RequestMapping("/api")
public class CriancaResource {

    private final Logger log = LoggerFactory.getLogger(CriancaResource.class);

    private static final String ENTITY_NAME = "crianca";

    private final CriancaRepository criancaRepository;

    private final CriancaSearchRepository criancaSearchRepository;

    public CriancaResource(CriancaRepository criancaRepository, CriancaSearchRepository criancaSearchRepository) {
        this.criancaRepository = criancaRepository;
        this.criancaSearchRepository = criancaSearchRepository;
    }

    /**
     * POST  /criancas : Create a new crianca.
     *
     * @param crianca the crianca to create
     * @return the ResponseEntity with status 201 (Created) and with body the new crianca, or with status 400 (Bad Request) if the crianca has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/criancas")
    @Timed
    public ResponseEntity<Crianca> createCrianca(@Valid @RequestBody Crianca crianca) throws URISyntaxException {
        log.debug("REST request to save Crianca : {}", crianca);
        if (crianca.getId() != null) {
            throw new BadRequestAlertException("A new crianca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Crianca result = criancaRepository.save(crianca);
        criancaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/criancas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /criancas : Updates an existing crianca.
     *
     * @param crianca the crianca to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated crianca,
     * or with status 400 (Bad Request) if the crianca is not valid,
     * or with status 500 (Internal Server Error) if the crianca couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/criancas")
    @Timed
    public ResponseEntity<Crianca> updateCrianca(@Valid @RequestBody Crianca crianca) throws URISyntaxException {
        log.debug("REST request to update Crianca : {}", crianca);
        if (crianca.getId() == null) {
            return createCrianca(crianca);
        }
        Crianca result = criancaRepository.save(crianca);
        criancaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, crianca.getId().toString()))
            .body(result);
    }

    /**
     * GET  /criancas : get all the criancas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of criancas in body
     */
    @GetMapping("/criancas")
    @Timed
    public List<Crianca> getAllCriancas() {
        log.debug("REST request to get all Criancas");
        return criancaRepository.findAll();
        }

    /**
     * GET  /criancas/:id : get the "id" crianca.
     *
     * @param id the id of the crianca to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the crianca, or with status 404 (Not Found)
     */
    @GetMapping("/criancas/{id}")
    @Timed
    public ResponseEntity<Crianca> getCrianca(@PathVariable Long id) {
        log.debug("REST request to get Crianca : {}", id);
        Crianca crianca = criancaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(crianca));
    }

    /**
     * DELETE  /criancas/:id : delete the "id" crianca.
     *
     * @param id the id of the crianca to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/criancas/{id}")
    @Timed
    public ResponseEntity<Void> deleteCrianca(@PathVariable Long id) {
        log.debug("REST request to delete Crianca : {}", id);
        criancaRepository.delete(id);
        criancaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/criancas?query=:query : search for the crianca corresponding
     * to the query.
     *
     * @param query the query of the crianca search
     * @return the result of the search
     */
    @GetMapping("/_search/criancas")
    @Timed
    public List<Crianca> searchCriancas(@RequestParam String query) {
        log.debug("REST request to search Criancas for query {}", query);
        return StreamSupport
            .stream(criancaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
