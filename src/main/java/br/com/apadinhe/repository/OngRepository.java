package br.com.apadinhe.repository;

import br.com.apadinhe.domain.Ong;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Ong entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {

}
