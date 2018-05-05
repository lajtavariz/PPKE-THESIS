package hu.ppke.yeast.service;

import hu.ppke.yeast.YeastApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
    EvaluationService evaluationService;

    @Test
    @Transactional
    @SuppressWarnings("unchecked")
    public void loadFiles_areOK() throws Exception {


    }


}
