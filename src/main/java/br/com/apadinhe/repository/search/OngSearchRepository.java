package br.com.apadinhe.repository.search;

import br.com.apadinhe.domain.Ong;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ong entity.
 */
public interface OngSearchRepository extends ElasticsearchRepository<Ong, Long> {
}
