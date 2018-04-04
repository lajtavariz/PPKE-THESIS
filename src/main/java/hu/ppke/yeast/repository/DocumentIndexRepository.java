package hu.ppke.yeast.repository;

import hu.ppke.yeast.domain.DocumentIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the DocumentIndex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentIndexRepository extends JpaRepository<DocumentIndex, Long> {

}
