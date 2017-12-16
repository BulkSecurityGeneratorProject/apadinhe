package br.com.apadinhe.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.apadinhe.domain.Doacao;

import br.com.apadinhe.repository.DoacaoRepository;
import br.com.apadinhe.repository.search.DoacaoSearchRepository;
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
 * REST controller for managing Doacao.
 */
@RestController
@RequestMapping("/api")
public class DoacaoResource {

    private final Logger log = LoggerFactory.getLogger(DoacaoResource.class);

    private static final String ENTITY_NAME = "doacao";

    private final DoacaoRepository doacaoRepository;

    private final DoacaoSearchRepository doacaoSearchRepository;

    public DoacaoResource(DoacaoRepository doacaoRepository, DoacaoSearchRepository doacaoSearchRepository) {
        this.doacaoRepository = doacaoRepository;
        this.doacaoSearchRepository = doacaoSearchRepository;
    }

    /**
     * POST  /doacaos : Create a new doacao.
     *
     * @param doacao the doacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new doacao, or with status 400 (Bad Request) if the doacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/doacaos")
    @Timed
    public ResponseEntity<Doacao> createDoacao(@Valid @RequestBody Doacao doacao) throws URISyntaxException {
        log.debug("REST request to save Doacao : {}", doacao);
        if (doacao.getId() != null) {
            throw new BadRequestAlertException("A new doacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Doacao result = doacaoRepository.save(doacao);
        doacaoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/doacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /doacaos : Updates an existing doacao.
     *
     * @param doacao the doacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated doacao,
     * or with status 400 (Bad Request) if the doacao is not valid,
     * or with status 500 (Internal Server Error) if the doacao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/doacaos")
    @Timed
    public ResponseEntity<Doacao> updateDoacao(@Valid @RequestBody Doacao doacao) throws URISyntaxException {
        log.debug("REST request to update Doacao : {}", doacao);
        if (doacao.getId() == null) {
            return createDoacao(doacao);
        }
        Doacao result = doacaoRepository.save(doacao);
        doacaoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, doacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /doacaos : get all the doacaos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of doacaos in body
     */
    @GetMapping("/doacaos")
    @Timed
    public List<Doacao> getAllDoacaos() {
        log.debug("REST request to get all Doacaos");
        return doacaoRepository.findAll();
        }

    /**
     * GET  /doacaos/:id : get the "id" doacao.
     *
     * @param id the id of the doacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the doacao, or with status 404 (Not Found)
     */
    @GetMapping("/doacaos/{id}")
    @Timed
    public ResponseEntity<Doacao> getDoacao(@PathVariable Long id) {
        log.debug("REST request to get Doacao : {}", id);
        Doacao doacao = doacaoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(doacao));
    }

    /**
     * DELETE  /doacaos/:id : delete the "id" doacao.
     *
     * @param id the id of the doacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/doacaos/{id}")
    @Timed
    public ResponseEntity<Void> deleteDoacao(@PathVariable Long id) {
        log.debug("REST request to delete Doacao : {}", id);
        doacaoRepository.delete(id);
        doacaoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/doacaos?query=:query : search for the doacao corresponding
     * to the query.
     *
     * @param query the query of the doacao search
     * @return the result of the search
     */
    @GetMapping("/_search/doacaos")
    @Timed
    public List<Doacao> searchDoacaos(@RequestParam String query) {
        log.debug("REST request to search Doacaos for query {}", query);
        return StreamSupport
            .stream(doacaoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
