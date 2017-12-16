package br.com.apadinhe.repository.search;

import br.com.apadinhe.domain.Doacao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Doacao entity.
 */
public interface DoacaoSearchRepository extends ElasticsearchRepository<Doacao, Long> {
}
