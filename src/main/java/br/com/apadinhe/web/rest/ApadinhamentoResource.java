package br.com.apadinhe.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.apadinhe.domain.Apadinhamento;

import br.com.apadinhe.repository.ApadinhamentoRepository;
import br.com.apadinhe.repository.search.ApadinhamentoSearchRepository;
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
 * REST controller for managing Apadinhamento.
 */
@RestController
@RequestMapping("/api")
public class ApadinhamentoResource {

    private final Logger log = LoggerFactory.getLogger(ApadinhamentoResource.class);

    private static final String ENTITY_NAME = "apadinhamento";

    private final ApadinhamentoRepository apadinhamentoRepository;

    private final ApadinhamentoSearchRepository apadinhamentoSearchRepository;

    public ApadinhamentoResource(ApadinhamentoRepository apadinhamentoRepository, ApadinhamentoSearchRepository apadinhamentoSearchRepository) {
        this.apadinhamentoRepository = apadinhamentoRepository;
        this.apadinhamentoSearchRepository = apadinhamentoSearchRepository;
    }

    /**
     * POST  /apadinhamentos : Create a new apadinhamento.
     *
     * @param apadinhamento the apadinhamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new apadinhamento, or with status 400 (Bad Request) if the apadinhamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/apadinhamentos")
    @Timed
    public ResponseEntity<Apadinhamento> createApadinhamento(@Valid @RequestBody Apadinhamento apadinhamento) throws URISyntaxException {
        log.debug("REST request to save Apadinhamento : {}", apadinhamento);
        if (apadinhamento.getId() != null) {
            throw new BadRequestAlertException("A new apadinhamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Apadinhamento result = apadinhamentoRepository.save(apadinhamento);
        apadinhamentoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/apadinhamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /apadinhamentos : Updates an existing apadinhamento.
     *
     * @param apadinhamento the apadinhamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated apadinhamento,
     * or with status 400 (Bad Request) if the apadinhamento is not valid,
     * or with status 500 (Internal Server Error) if the apadinhamento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/apadinhamentos")
    @Timed
    public ResponseEntity<Apadinhamento> updateApadinhamento(@Valid @RequestBody Apadinhamento apadinhamento) throws URISyntaxException {
        log.debug("REST request to update Apadinhamento : {}", apadinhamento);
        if (apadinhamento.getId() == null) {
            return createApadinhamento(apadinhamento);
        }
        Apadinhamento result = apadinhamentoRepository.save(apadinhamento);
        apadinhamentoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, apadinhamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /apadinhamentos : get all the apadinhamentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of apadinhamentos in body
     */
    @GetMapping("/apadinhamentos")
    @Timed
    public List<Apadinhamento> getAllApadinhamentos() {
        log.debug("REST request to get all Apadinhamentos");
        return apadinhamentoRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /apadinhamentos/:id : get the "id" apadinhamento.
     *
     * @param id the id of the apadinhamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the apadinhamento, or with status 404 (Not Found)
     */
    @GetMapping("/apadinhamentos/{id}")
    @Timed
    public ResponseEntity<Apadinhamento> getApadinhamento(@PathVariable Long id) {
        log.debug("REST request to get Apadinhamento : {}", id);
        Apadinhamento apadinhamento = apadinhamentoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(apadinhamento));
    }

    /**
     * DELETE  /apadinhamentos/:id : delete the "id" apadinhamento.
     *
     * @param id the id of the apadinhamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/apadinhamentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteApadinhamento(@PathVariable Long id) {
        log.debug("REST request to delete Apadinhamento : {}", id);
        apadinhamentoRepository.delete(id);
        apadinhamentoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/apadinhamentos?query=:query : search for the apadinhamento corresponding
     * to the query.
     *
     * @param query the query of the apadinhamento search
     * @return the result of the search
     */
    @GetMapping("/_search/apadinhamentos")
    @Timed
    public List<Apadinhamento> searchApadinhamentos(@RequestParam String query) {
        log.debug("REST request to search Apadinhamentos for query {}", query);
        return StreamSupport
            .stream(apadinhamentoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
