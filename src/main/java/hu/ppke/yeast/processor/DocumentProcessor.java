package hu.ppke.yeast.processor;

import com.opencsv.CSVReaderBuilder;
import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.Index;
import hu.ppke.yeast.repository.IndexRepository;
import hu.ppke.yeast.service.DocumentIndexService;
import opennlp.tools.stemmer.PorterStemmer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for the following steps in the text processing:
 * 1. Get the words from a given document
 * 2. Apply the stoplist on the list of words (remove those ones which are on the stoplist)
 * 3. Apply the stemmer algorithm on the remaining words
 * 4. Persist the indices
 */
@Component
public class DocumentProcessor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ResourceLoader resourceLoader;
    private final IndexRepository indexRepository;
    private final DocumentIndexService docIndexService;

    private List<String> stopWords;

    @Autowired
    public DocumentProcessor(ResourceLoader resourceLoader,
                             IndexRepository indexRepository,
                             DocumentIndexService docIndexService) {
        this.resourceLoader = resourceLoader;
        this.indexRepository = indexRepository;
        this.docIndexService = docIndexService;
    }

    @PostConstruct
    public void init() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + "txt_processing/stop_words.csv");
        log.debug("Loading stopwords from stop_words.csv file");
        String[] stopWordsArray = new CSVReaderBuilder(new InputStreamReader(resource.getInputStream())).build().readNext();
        stopWords = Arrays.asList(stopWordsArray);
    }

    public void processDocument(Document document) {
        List<String> rawIndeces = stemWords(filterWords(getWords(document.getContent())));
        persistIndices(document, rawIndeces);
    }

    private List<String> getWords(String documentContent) {
        // We get the first version of the words by splitting the document at the commas
        String[] words = documentContent.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            // We replace all characters apart from [a-zA-Z] with an empty character
            words[i] = words[i].replaceAll("[^a-zA-Z]", "").toLowerCase();
        }

        return Arrays.asList(words);
    }

    private List<String> filterWords(List<String> words) {
        // Exclude those words which are on the stoplist
        return words.stream().filter(w -> !stopWords.contains(w)).collect(Collectors.toList());
    }

    private List<String> stemWords(List<String> words) {
        PorterStemmer stemmer = new PorterStemmer();

        for (int i = 0; i < words.size(); i++) {
            words.set(i, stemmer.stem(words.get(i)));
        }

        return words;
    }

    private void persistIndices(Document document, List<String> rawIndeces) {
        Set<String> uniqueIndices = new HashSet<String>(rawIndeces);
        for (String key : uniqueIndices) {
            Index index;
            index = indexRepository.findByName(key);
            if (index == null) {
                index = indexRepository.save(new Index().setName(key).setDocumentCount(0L));
            } else {
                index.setDocumentCount(index.getDocumentCount() + 1);
            }
            int freq = Collections.frequency(rawIndeces, key);

            docIndexService.save(document, index, freq);
        }
    }

}
