package hu.ppke.yeast.service.impl;

import com.opencsv.CSVReader;
import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.service.DocumentIndexService;
import hu.ppke.yeast.service.DocumentService;
import hu.ppke.yeast.service.EvaluationService;
import hu.ppke.yeast.service.dto.DocumentSearchResultDTO;
import hu.ppke.yeast.service.dto.EvaluationResultDTO;
import hu.ppke.yeast.service.dto.evaluation.ADIArticle;
import hu.ppke.yeast.service.dto.evaluation.ADIQuery;
import hu.ppke.yeast.service.dto.evaluation.RecallPrecision;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hu.ppke.yeast.calculator.SimilarityEvaluationCalculator.*;

/**
 * Service Implementation for the similarity measure evaluation
 */
@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ResourceLoader resourceLoader;
    private final DocumentIndexService documentIndexService;
    private final DocumentService documentService;

    private List<ADIArticle> articles = new ArrayList<>();
    private List<ADIQuery> queries = new ArrayList<>();

    @Autowired
    public EvaluationServiceImpl(ResourceLoader resourceLoader,
                                 DocumentIndexService documentIndexService,
                                 DocumentService documentService) {
        this.resourceLoader = resourceLoader;
        this.documentIndexService = documentIndexService;
        this.documentService = documentService;
    }

    @PostConstruct
    public void init() throws Exception {
        loadArticles();
        loadQueries();
        loadRels();
    }

    private void loadArticles() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + "similarity_eval/adi/ADI.ALL");
        log.debug("Loading articles from ADI test collection");
        String readString = IOUtils.toString(resource.getInputStream(), "UTF-8");
        String[] articles = readString.split("\\.I");

        for (int i = 1; i < articles.length; i++) {
            String article = articles[i];
            String[] splitArticle = article.split("\\.T|\\.A|\\.W");

            this.articles.add(new ADIArticle()
                .setId(Long.parseLong(splitArticle[0].replaceAll("[^\\d]", "")))
                .setTitle(splitArticle[1])
                .setContent(splitArticle[splitArticle.length - 1]));
        }
    }

    private void loadQueries() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + "similarity_eval/adi/ADI.QRY");
        log.debug("Loading queries from ADI test collection");
        String readString = IOUtils.toString(resource.getInputStream(), "UTF-8");
        String[] queries = readString.split("\\.I");

        for (int i = 1; i < queries.length; i++) {
            String query = queries[i];
            String[] splitQuery = query.split("\\.W");

            this.queries.add(new ADIQuery()
                .setId(Integer.parseInt(splitQuery[0].replaceAll("[^\\d]", "")))
                .setQuery(splitQuery[1]));
        }
    }

    private void loadRels() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + "similarity_eval/adi/ADI.REL");
        log.debug("Loading rels from ADI test collection");
        CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()));
        List<String[]> records = csvReader.readAll();
        assert records.size() == 170;

        for (ADIQuery adiQuery : queries) {
            List<String[]> filteredRecords = records.stream()
                .filter(p -> Integer.parseInt(p[0]) == adiQuery.getId())
                .collect(Collectors.toList());

            for (String[] record : filteredRecords) {
                adiQuery.addRelevantDocument(Long.parseLong(record[1]));
            }
        }
    }

    @Override
    public EvaluationResultDTO evaluate(int measure) {
        documentIndexService.clearDB();
        EvaluationResultDTO evaluationResult = new EvaluationResultDTO();

        List<Document> documentsToSave = new ArrayList<>();
        for (ADIArticle article : articles) {
            Document document = new Document()
                .setCreation_date(LocalDate.now())
                .setEvaluationId(article.getId())
                .setContent(article.getTitle() + " " + article.getContent());

            documentsToSave.add(document);
        }

        documentService.saveMultiple(documentsToSave);

        List<List<RecallPrecision>> allRecallPrecisionLists = new ArrayList<>();

        for (ADIQuery query : queries) {
            List<DocumentSearchResultDTO> searchResults = documentService.search(query.getQuery(), measure);
            evaluationResult.addQueryStatistic(generateQueryStatistic(searchResults, query));
            allRecallPrecisionLists.add(calculateRecallPrecisionList(searchResults, query));
        }

        evaluationResult.setRecallPrecisions(calculateAverageRecallPrecisionList(allRecallPrecisionLists));

        return evaluationResult;
    }
}
