package br.com.apadinhe.repository;

import br.com.apadinhe.domain.Apadinhamento;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Apadinhamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApadinhamentoRepository extends JpaRepository<Apadinhamento, Long> {
    @Query("select distinct apadinhamento from Apadinhamento apadinhamento left join fetch apadinhamento.padrinhos left join fetch apadinhamento.criancas")
    List<Apadinhamento> findAllWithEagerRelationships();

    @Query("select apadinhamento from Apadinhamento apadinhamento left join fetch apadinhamento.padrinhos left join fetch apadinhamento.criancas where apadinhamento.id =:id")
    Apadinhamento findOneWithEagerRelationships(@Param("id") Long id);

}
