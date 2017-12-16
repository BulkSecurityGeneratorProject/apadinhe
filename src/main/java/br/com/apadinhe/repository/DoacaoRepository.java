package br.com.apadinhe.repository;

import br.com.apadinhe.domain.Doacao;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Doacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    @Query("select doacao from Doacao doacao where doacao.doador.login = ?#{principal.username}")
    List<Doacao> findByDoadorIsCurrentUser();

}
