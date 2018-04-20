package hu.ppke.yeast.processor;

import hu.ppke.yeast.YeastApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YeastApp.class)
@Transactional
public class DocumentProcessorTest {

    private final String sampleText1 = "Among going{} [manor; who did.1 Do ye is -@celebrated it> sympathize, 8considered.";

    @Autowired
    DocumentProcessor documentProcessor;

    @Test
    public void getWords_returnsOnlyTheWordsFromString() throws Exception {
        //System.out.println(documentProcessor.processDocument(sampleText1));
    }


}
