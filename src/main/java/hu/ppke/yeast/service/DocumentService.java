package hu.ppke.yeast.service;

import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.service.dto.DocumentDTO;
import hu.ppke.yeast.service.dto.DocumentSearchResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Document.
 */
public interface DocumentService {

    /**
     * Save a document.
     *
     * @param documentDTO the entity to save
     * @return the persisted entity
     */
    DocumentDTO save(DocumentDTO documentDTO);

    /**
     * Save a document.
     *
     * @param document the entity to save
     * @return the persisted entity
     */
    Document save(Document document);


    /**
     * Get all the documents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DocumentDTO> findAll(Pageable pageable);

    /**
     * Get the "id" document.
     *
     * @param id the id of the entity
     * @return the entity
     */
    DocumentDTO findOne(Long id);

    /**
     * Get the documents relevant to the query.
     *
     * @param query  the search query
     * @param measure the chosen metric
     * @return the list of entities
     */
    List<DocumentSearchResultDTO> search(String query, int measure);

    /**
     * Delete the "id" document.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
