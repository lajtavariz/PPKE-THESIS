package hu.ppke.yeast.service;

import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.DocumentIndex;
import hu.ppke.yeast.domain.Index;

/**
 * Service Interface for managing DocumentIndex.
 */
public interface DocumentIndexService {

    /**
     * @param document document
     * @param index    corresponding index
     * @param count    frequency of index in document
     */
    DocumentIndex save(Document document, Index index, long count);

}
