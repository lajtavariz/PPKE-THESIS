package hu.ppke.yeast.service.impl;

import com.opencsv.CSVReaderBuilder;
import hu.ppke.yeast.enumeration.SimiliarityMeasure;
import hu.ppke.yeast.service.EvaluationService;
import hu.ppke.yeast.service.dto.EvaluationResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for the similarity measure evaluation
 */
@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ResourceLoader resourceLoader;

    private List<String> stopWords;

    @Autowired
    public EvaluationServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + "txt_processing/stop_words.csv");
        log.debug("Loading stopwords from stop_words.csv file");
        String[] stopWordsArray = new CSVReaderBuilder(new InputStreamReader(resource.getInputStream())).build().readNext();
        stopWords = Arrays.asList(stopWordsArray);
    }

    @Override
    public EvaluationResultDTO evaluate(int measure) {
        SimiliarityMeasure similiarityMeasure = SimiliarityMeasure.getMeasure(measure);


        return null;
    }
}
