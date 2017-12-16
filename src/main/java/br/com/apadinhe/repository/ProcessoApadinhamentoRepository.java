package br.com.apadinhe.repository;

import br.com.apadinhe.domain.ProcessoApadinhamento;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the ProcessoApadinhamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessoApadinhamentoRepository extends JpaRepository<ProcessoApadinhamento, Long> {
    @Query("select distinct processo_apadinhamento from ProcessoApadinhamento processo_apadinhamento left join fetch processo_apadinhamento.padrinhos left join fetch processo_apadinhamento.criancas")
    List<ProcessoApadinhamento> findAllWithEagerRelationships();

    @Query("select processo_apadinhamento from ProcessoApadinhamento processo_apadinhamento left join fetch processo_apadinhamento.padrinhos left join fetch processo_apadinhamento.criancas where processo_apadinhamento.id =:id")
    ProcessoApadinhamento findOneWithEagerRelationships(@Param("id") Long id);

}
