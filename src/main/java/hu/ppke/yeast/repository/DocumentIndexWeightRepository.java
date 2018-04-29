package hu.ppke.yeast.repository;


import hu.ppke.yeast.domain.DocumentIndexWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the DocumentIndexWeight entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentIndexWeightRepository extends JpaRepository<DocumentIndexWeight, Long> {

    List<DocumentIndexWeight> findByDocumentIdOrderByIndexIdAsc(Long documentId);
}
