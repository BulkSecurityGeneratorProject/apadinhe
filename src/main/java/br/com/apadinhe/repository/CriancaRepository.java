package br.com.apadinhe.repository;

import br.com.apadinhe.domain.Crianca;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Crianca entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CriancaRepository extends JpaRepository<Crianca, Long> {

}
