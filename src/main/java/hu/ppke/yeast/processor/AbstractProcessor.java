package hu.ppke.yeast.processor;

import com.opencsv.CSVReaderBuilder;
import hu.ppke.yeast.repository.IndexRepository;
import opennlp.tools.stemmer.PorterStemmer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractProcessor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ResourceLoader resourceLoader;
    protected final IndexRepository indexRepository;

    private List<String> stopWords;

    @Autowired
    public AbstractProcessor(ResourceLoader resourceLoader, IndexRepository indexRepository) {
        this.resourceLoader = resourceLoader;
        this.indexRepository = indexRepository;
    }

    @PostConstruct
    public void init() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + "txt_processing/stop_words.csv");
        log.debug("Loading stopwords from stop_words.csv file");
        String[] stopWordsArray = new CSVReaderBuilder(new InputStreamReader(resource.getInputStream())).build().readNext();
        stopWords = Arrays.asList(stopWordsArray);
    }

    protected List<String> getWords(String documentContent) {
        // We get the first version of the words by splitting the document at the commas
        String[] words = documentContent.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            // We replace all characters apart from [a-zA-Z] with an empty character
            words[i] = words[i].replaceAll("[^a-zA-Z]", "").toLowerCase();
        }

        return Arrays.asList(words);
    }

    protected List<String> filterWords(List<String> words) {
        // Exclude those words which are on the stoplist
        return words.stream().filter(w -> !stopWords.contains(w)).collect(Collectors.toList());
    }

    protected List<String> stemWords(List<String> words) {
        PorterStemmer stemmer = new PorterStemmer();

        for (int i = 0; i < words.size(); i++) {
            words.set(i, stemmer.stem(words.get(i)));
        }

        return words;
    }
}
