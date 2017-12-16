package br.com.apadinhe.repository;

import br.com.apadinhe.domain.Visita;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Visita entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    @Query("select visita from Visita visita where visita.padrinho.login = ?#{principal.username}")
    List<Visita> findByPadrinhoIsCurrentUser();

}
