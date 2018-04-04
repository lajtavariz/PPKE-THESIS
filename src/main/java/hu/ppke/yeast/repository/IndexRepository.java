package hu.ppke.yeast.repository;

import hu.ppke.yeast.domain.Index;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Index entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexRepository extends JpaRepository<Index, Long> {

}
