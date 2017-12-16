package br.com.apadinhe.repository.search;

import br.com.apadinhe.domain.Apadinhamento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Apadinhamento entity.
 */
public interface ApadinhamentoSearchRepository extends ElasticsearchRepository<Apadinhamento, Long> {
}
