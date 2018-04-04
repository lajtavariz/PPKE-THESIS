package hu.ppke.yeast.repository;

import hu.ppke.yeast.YeastApp;
import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.DocumentIndex;
import hu.ppke.yeast.domain.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Test class for the Document, Index and DocumentIndex repository
 *
 * @see DocumentRepository
 * @see IndexRepository
 * @see DocumentIndexRepository
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YeastApp.class)
@Transactional
public class DocumentIndexRepositoryIntTest {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    IndexRepository indexRepository;

    @Autowired
    DocumentIndexRepository docIndexRepository;

    @Test
    public void test1() {
        Document document = new Document();
        document.setCreation_date(LocalDate.now());
        document.setContent("blabal");

        Index index = new Index();
        index.setName("bla");
        index.setTotal_count(1L);

        DocumentIndex documentIndex = new DocumentIndex();
        documentIndex.setDocument(document);
        documentIndex.setIndex(index);
        documentIndex.setCount(1L);
        documentIndex.setWeight(0.5);

        document.getDocumentIndices().add(documentIndex);
        index.getDocumentIndices().add(documentIndex);

        Index persistedIndex = indexRepository.saveAndFlush(index);
        Document persistedDocument = documentRepository.saveAndFlush(document);
        docIndexRepository.saveAndFlush(documentIndex);

        List<Document> persistedDocuments = documentRepository.findAll();
        assertThat(persistedDocuments).containsOnly(persistedDocument);

        List<Index> persistedIndices = indexRepository.findAll();
        assertThat(persistedIndices).containsOnly(persistedIndex);

        List<DocumentIndex> documentIndices = docIndexRepository.findAll();
        assertThat(documentIndices).hasSize(1);

    }


}
