package hu.ppke.yeast.service;

import hu.ppke.yeast.YeastApp;
import hu.ppke.yeast.service.dto.evaluation.ADIArticle;
import hu.ppke.yeast.service.dto.evaluation.ADIQuery;
import hu.ppke.yeast.service.impl.EvaluationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the EvaluationService service.
 *
 * @see EvaluationService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YeastApp.class)
@Transactional
public class EvaluationServiceIntTest {

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    DocumentIndexService documentIndexService;

    @Autowired
    DocumentService documentService;

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    public void loadFiles_areOK() throws Exception {
        EvaluationServiceImpl service = new EvaluationServiceImpl(resourceLoader, documentIndexService, documentService);
        service.init();

        // Get the list of articles via reflection
        Field field = service.getClass().getDeclaredField("articles");
        field.setAccessible(true);
        List<ADIArticle> articles = (ArrayList<ADIArticle>) field.get(service);

        // Validate
        assertThat(articles).hasSize(82);
        assertThat(articles.get(0).getId()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo("\r\nthe ibm dsd technical information center - a total systems approach\r\n" +
            "combining traditional library features\r\n" +
            "and mechanized computer processing\r\n");
        assertThat(articles.get(81).getId()).isEqualTo(82);
        assertThat(articles.get(81).getTitle()).isEqualTo("\r\nmachine recognition of linguistic synonymy and logical deducibility .\r\n");
        assertThat(articles.get(66).getContent()).isEqualTo("\r\na national plan for science abstracting and indexing services is\r\n" +
            "presented .  both profession oriented services and project oriented\r\n" +
            "services are considered .  market, volume and cost are discussed .\r\n");

        // Get the list of queries via reflection
        field = service.getClass().getDeclaredField("queries");
        field.setAccessible(true);
        List<ADIQuery> queries = (ArrayList<ADIQuery>) field.get(service);

        assertThat(queries).hasSize(35);
        assertThat(queries.get(0).getId()).isEqualTo(1);
        assertThat(queries.get(34).getId()).isEqualTo(35);
        assertThat(queries.get(24).getQuery()).isEqualTo("\r\nInternational systems for exchange and dissemination of information.\r\n");

        assertThat(queries.get(0).getRelevantDocuments()).isEqualTo(Arrays.asList(17L, 46L, 62L));
        assertThat(queries.get(24).getRelevantDocuments()).isEqualTo(Arrays.asList(13L, 24L, 53L));
        assertThat(queries.get(34).getRelevantDocuments()).isEqualTo(Arrays.asList(13L, 24L, 52L, 53L, 56L, 59L, 67L, 79L));
    }
}
