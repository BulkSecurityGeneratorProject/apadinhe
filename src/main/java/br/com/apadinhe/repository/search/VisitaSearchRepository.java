package br.com.apadinhe.repository.search;

import br.com.apadinhe.domain.Visita;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Visita entity.
 */
public interface VisitaSearchRepository extends ElasticsearchRepository<Visita, Long> {
}
