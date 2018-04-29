package hu.ppke.yeast.processor;

import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.Index;
import hu.ppke.yeast.repository.IndexRepository;
import hu.ppke.yeast.service.DocumentIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is responsible for the following steps in the text processing:
 * 1. Get the words from a given document
 * 2. Apply the stoplist on the list of words (remove those ones which are on the stoplist)
 * 3. Apply the stemmer algorithm on the remaining words
 * 4. Persist the indices
 */
@Component
public class DocumentProcessor extends AbstractProcessor {

    private final IndexRepository indexRepository;
    private final DocumentIndexService docIndexService;

    @Autowired
    public DocumentProcessor(ResourceLoader resourceLoader,
                             IndexRepository indexRepository,
                             DocumentIndexService docIndexService) {
        super(resourceLoader);
        this.indexRepository = indexRepository;
        this.docIndexService = docIndexService;
    }

    public void processDocument(Document document) {
        List<String> rawIndeces = stemWords(filterWords(getWords(document.getContent())));
        persistIndices(document, rawIndeces);
    }

    private void persistIndices(Document document, List<String> rawIndeces) {
        Set<String> uniqueIndices = new HashSet<String>(rawIndeces);
        for (String key : uniqueIndices) {
            Index index;
            index = indexRepository.findByName(key);
            if (index == null) {
                index = indexRepository.save(new Index().setName(key).setDocumentCount(1L));
            } else {
                index.setDocumentCount(index.getDocumentCount() + 1);
            }
            int freq = Collections.frequency(rawIndeces, key);

            docIndexService.save(document, index, freq);
        }
    }

}
