package br.com.apadinhe.repository.search;

import br.com.apadinhe.domain.Crianca;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Crianca entity.
 */
public interface CriancaSearchRepository extends ElasticsearchRepository<Crianca, Long> {
}
