package hu.ppke.yeast.repository;


import hu.ppke.yeast.domain.DocumentIndexWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentIndexWeight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentIndexWeightRepository extends JpaRepository<DocumentIndexWeight, Long> {
}
