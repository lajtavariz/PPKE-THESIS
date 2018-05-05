package hu.ppke.yeast.service.impl;

import hu.ppke.yeast.enumeration.SimiliarityMeasure;
import hu.ppke.yeast.service.EvaluationService;
import hu.ppke.yeast.service.dto.evaluation.ADIArticle;
import hu.ppke.yeast.service.dto.evaluation.ADIQuery;
import hu.ppke.yeast.service.dto.evaluation.EvaluationResultDTO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for the similarity measure evaluation
 */
@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ResourceLoader resourceLoader;

    private List<ADIArticle> adiArticles = new ArrayList<>();
    private List<ADIQuery> adiQueries = new ArrayList<>();

    @Autowired
    public EvaluationServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() throws Exception {
        loadArticles();
        validateArticles();

    }

    private void loadArticles() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:" + "similarity_eval/adi/ADI.ALL");
        log.debug("Loading documents from ADI test collection...");
        String adiAll = IOUtils.toString(resource.getInputStream(), "UTF-8");
        String[] articles = adiAll.split("\\.I");

        for (int i = 1; i < articles.length; i++) {
            String article = articles[i];
            String[] splittedArticle = article.split("\\.T|\\.A|\\.W");

            adiArticles.add(new ADIArticle()
                .setId(Integer.parseInt(splittedArticle[0].replaceAll("[^\\d]", "")))
                .setTitle(splittedArticle[1])
                .setContent(splittedArticle[splittedArticle.length - 1]));
        }
    }

    private void validateArticles() throws Exception {
        assert adiArticles.size() == 82;
        assert adiArticles.get(0).getId() == 1;
        assert adiArticles.get(0).getTitle().equals("\r\nthe ibm dsd technical information center - a total systems approach\r\n" +
            "combining traditional library features\r\n" +
            "and mechanized computer processing\r\n");
        assert adiArticles.get(81).getId() == 82;
        assert adiArticles.get(81).getTitle().equals("\r\nmachine recognition of linguistic synonymy and logical deducibility .\r\n");
        assert adiArticles.get(66).getContent().equals("\r\na national plan for science abstracting and indexing services is\r\n" +
            "presented .  both profession oriented services and project oriented\r\n" +
            "services are considered .  market, volume and cost are discussed .\r\n");
    }

    @Override
    public EvaluationResultDTO evaluate(int measure) {
        SimiliarityMeasure similiarityMeasure = SimiliarityMeasure.getMeasure(measure);


        return null;
    }
}
