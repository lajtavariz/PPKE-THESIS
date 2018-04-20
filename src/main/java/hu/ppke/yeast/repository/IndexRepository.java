package hu.ppke.yeast.repository;

import hu.ppke.yeast.domain.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Index entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndexRepository extends JpaRepository<Index, Long> {

    Index findByName(String name);

}
