package hu.ppke.yeast.service;

import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.DocumentIndexWeight;

import java.util.List;

/**
 * Service Interface for managing DocumentIndexWeight.
 */
public interface DocumentIndexWeightService {

    /**
     * Save document and corresponding weights to the DB
     *
     * @param document        document
     * @param docIndexWeights document - index - weight pairs
     */
    void save(Document document, List<DocumentIndexWeight> docIndexWeights);


}
