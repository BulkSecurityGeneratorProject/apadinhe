package br.com.apadinhe.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.apadinhe.domain.ProcessoApadinhamento;

import br.com.apadinhe.repository.ProcessoApadinhamentoRepository;
import br.com.apadinhe.repository.search.ProcessoApadinhamentoSearchRepository;
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
 * REST controller for managing ProcessoApadinhamento.
 */
@RestController
@RequestMapping("/api")
public class ProcessoApadinhamentoResource {

    private final Logger log = LoggerFactory.getLogger(ProcessoApadinhamentoResource.class);

    private static final String ENTITY_NAME = "processoApadinhamento";

    private final ProcessoApadinhamentoRepository processoApadinhamentoRepository;

    private final ProcessoApadinhamentoSearchRepository processoApadinhamentoSearchRepository;

    public ProcessoApadinhamentoResource(ProcessoApadinhamentoRepository processoApadinhamentoRepository, ProcessoApadinhamentoSearchRepository processoApadinhamentoSearchRepository) {
        this.processoApadinhamentoRepository = processoApadinhamentoRepository;
        this.processoApadinhamentoSearchRepository = processoApadinhamentoSearchRepository;
    }

    /**
     * POST  /processo-apadinhamentos : Create a new processoApadinhamento.
     *
     * @param processoApadinhamento the processoApadinhamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new processoApadinhamento, or with status 400 (Bad Request) if the processoApadinhamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/processo-apadinhamentos")
    @Timed
    public ResponseEntity<ProcessoApadinhamento> createProcessoApadinhamento(@Valid @RequestBody ProcessoApadinhamento processoApadinhamento) throws URISyntaxException {
        log.debug("REST request to save ProcessoApadinhamento : {}", processoApadinhamento);
        if (processoApadinhamento.getId() != null) {
            throw new BadRequestAlertException("A new processoApadinhamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessoApadinhamento result = processoApadinhamentoRepository.save(processoApadinhamento);
        processoApadinhamentoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/processo-apadinhamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /processo-apadinhamentos : Updates an existing processoApadinhamento.
     *
     * @param processoApadinhamento the processoApadinhamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated processoApadinhamento,
     * or with status 400 (Bad Request) if the processoApadinhamento is not valid,
     * or with status 500 (Internal Server Error) if the processoApadinhamento couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/processo-apadinhamentos")
    @Timed
    public ResponseEntity<ProcessoApadinhamento> updateProcessoApadinhamento(@Valid @RequestBody ProcessoApadinhamento processoApadinhamento) throws URISyntaxException {
        log.debug("REST request to update ProcessoApadinhamento : {}", processoApadinhamento);
        if (processoApadinhamento.getId() == null) {
            return createProcessoApadinhamento(processoApadinhamento);
        }
        ProcessoApadinhamento result = processoApadinhamentoRepository.save(processoApadinhamento);
        processoApadinhamentoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, processoApadinhamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /processo-apadinhamentos : get all the processoApadinhamentos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of processoApadinhamentos in body
     */
    @GetMapping("/processo-apadinhamentos")
    @Timed
    public List<ProcessoApadinhamento> getAllProcessoApadinhamentos() {
        log.debug("REST request to get all ProcessoApadinhamentos");
        return processoApadinhamentoRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /processo-apadinhamentos/:id : get the "id" processoApadinhamento.
     *
     * @param id the id of the processoApadinhamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the processoApadinhamento, or with status 404 (Not Found)
     */
    @GetMapping("/processo-apadinhamentos/{id}")
    @Timed
    public ResponseEntity<ProcessoApadinhamento> getProcessoApadinhamento(@PathVariable Long id) {
        log.debug("REST request to get ProcessoApadinhamento : {}", id);
        ProcessoApadinhamento processoApadinhamento = processoApadinhamentoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(processoApadinhamento));
    }

    /**
     * DELETE  /processo-apadinhamentos/:id : delete the "id" processoApadinhamento.
     *
     * @param id the id of the processoApadinhamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/processo-apadinhamentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcessoApadinhamento(@PathVariable Long id) {
        log.debug("REST request to delete ProcessoApadinhamento : {}", id);
        processoApadinhamentoRepository.delete(id);
        processoApadinhamentoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/processo-apadinhamentos?query=:query : search for the processoApadinhamento corresponding
     * to the query.
     *
     * @param query the query of the processoApadinhamento search
     * @return the result of the search
     */
    @GetMapping("/_search/processo-apadinhamentos")
    @Timed
    public List<ProcessoApadinhamento> searchProcessoApadinhamentos(@RequestParam String query) {
        log.debug("REST request to search ProcessoApadinhamentos for query {}", query);
        return StreamSupport
            .stream(processoApadinhamentoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
