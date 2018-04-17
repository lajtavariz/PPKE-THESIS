package hu.ppke.yeast.processor;

import com.opencsv.CSVReader;
import opennlp.tools.stemmer.PorterStemmer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for the following steps in the text processing:
 * 1. Get the words from a given document
 * 2. Apply the stoplist on the list of words (remove those ones which are on the stoplist)
 * 3. Apply the stemmer algorithm on the remaining words
 */
@Component
public class DocumentProcessor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ResourceLoader resourceLoader;
    private List<String> stopWords;

    @PostConstruct
    public void init() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + "txt_processing/stop_words.csv");
        log.debug("Loading stopwords from stop_words.csv file");
        String[] stopWordsArray = new CSVReader(new InputStreamReader(resource.getInputStream()), ',').readNext();
        stopWords = Arrays.asList(stopWordsArray);
    }


    public List<String> processDocument(String documentContent) {

        return stemWords(filterWords(getWords(documentContent)));
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

        return words.stream().filter(w -> !stopWords.contains(w)).collect(Collectors.toList());
    }

    private List<String> stemWords(List<String> words) {
        PorterStemmer stemmer = new PorterStemmer();

        for (int i = 0; i < words.size(); i++) {
            words.set(i, stemmer.stem(words.get(i)));
        }

        return words;
    }

}
