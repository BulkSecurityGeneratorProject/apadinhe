package br.com.apadinhe.repository.search;

import br.com.apadinhe.domain.ProcessoApadinhamento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProcessoApadinhamento entity.
 */
public interface ProcessoApadinhamentoSearchRepository extends ElasticsearchRepository<ProcessoApadinhamento, Long> {
}
