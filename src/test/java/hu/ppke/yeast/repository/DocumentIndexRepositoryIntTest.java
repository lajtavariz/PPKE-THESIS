package hu.ppke.yeast.repository;

import hu.ppke.yeast.YeastApp;
import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.DocumentIndex;
import hu.ppke.yeast.domain.Index;
import hu.ppke.yeast.web.rest.util.RandomString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    IndexRepository indexRepository;

    @Autowired
    DocumentIndexRepository docIndexRepository;


    @Before
    public void initTest() {


    }



    @Test
    @Transactional
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

    @Test
    @Transactional
    public void persistFixedNrOfDocumentsAndIndices_dBEntriesAreOK() {
        int nrOfDocuments = 10;
        int nrOfIndicesForDocument = 50;
        int nrOfCommonIndices = 20;

        persistDocumentsWithIndices(nrOfDocuments, nrOfIndicesForDocument, nrOfCommonIndices);

        List<Document> documents = documentRepository.findAll();
        assertThat(documents).hasSize(nrOfDocuments);

        List<Index> indices = indexRepository.findAll();
        assertThat(indices).hasSize(nrOfDocuments * (nrOfIndicesForDocument - nrOfCommonIndices) + nrOfCommonIndices);

        List<DocumentIndex> documentIndices = docIndexRepository.findAll();
        assertThat(documentIndices).hasSize(nrOfDocuments * nrOfIndicesForDocument);

        for (Document document : documents) {
            List<DocumentIndex> documentIndicesForDocument = new ArrayList<>(document.getDocumentIndices());
            assertThat(documentIndicesForDocument).hasSize(nrOfIndicesForDocument);

            Index firstIndex = documentIndicesForDocument.get(0).getIndex();
            assertThat(firstIndex.getId()).isNotNull();
        }

        List<Index> specificIndices = indices.stream()
            .filter(index -> index.getDocumentIndices().size() == 1)
            .collect(Collectors.toList());

        assertThat(specificIndices).hasSize(nrOfDocuments * (nrOfIndicesForDocument - nrOfCommonIndices));

        for (Index index : specificIndices) {
            List<DocumentIndex> documentIndicesForIndex = new ArrayList<>(index.getDocumentIndices());
            assertThat(index.getDocumentIndices()).hasSize(1);

            Document firstDocument = documentIndicesForIndex.get(0).getDocument();
            assertThat(firstDocument.getId()).isNotNull();
        }

        List<Index> commonIndices = indices.stream()
            .filter(index -> index.getDocumentIndices().size() > 1)
            .collect(Collectors.toList());

        assertThat(commonIndices).hasSize(nrOfCommonIndices);

        for (Index index : commonIndices) {
            List<DocumentIndex> documentIndicesForIndex = new ArrayList<>(index.getDocumentIndices());
            assertThat(index.getDocumentIndices()).hasSize(nrOfDocuments);

            Document firstDocument = documentIndicesForIndex.get(0).getDocument();
            assertThat(firstDocument.getId()).isNotNull();
        }
    }

    private void persistDocumentsWithIndices(int nrOfDocuments, int nrOfIndicesForDocument, int nrOfCommonIndices) {
        List<Index> commonIndices = new ArrayList<>();
        for (int i = 0; i < nrOfCommonIndices; i++) {
            commonIndices.add(generateIndex());
        }
        log.info("Persisting common indices...");
        commonIndices = indexRepository.save(commonIndices);

        for (int j = 0; j < nrOfDocuments; j++) {
            log.info("Persisting document nr. " + (j + 1) + "/" + nrOfDocuments + " and related indices...");
            Document document = documentRepository.saveAndFlush(generateDocument());

            List<DocumentIndex> documentIndices = new ArrayList<>();
            for (int k = 0; k < nrOfIndicesForDocument; k++) {
                Index index;

                if (k < nrOfCommonIndices) {
                    index = commonIndices.get(k);
                } else {
                    index = indexRepository.saveAndFlush(generateIndex());
                }

                documentIndices.add(generateDocumentIndex(document, index));

                DocumentIndex documentIndex = docIndexRepository.saveAndFlush(generateDocumentIndex(document, index));

                document.getDocumentIndices().add(documentIndex);
                index.getDocumentIndices().add(documentIndex);
                documentRepository.saveAndFlush(document);
                indexRepository.saveAndFlush(index);
            }
        }
    }


    private Index generateIndex() {
        return new Index().setName(new RandomString(5).nextString());
    }

    private Document generateDocument() {
        return new Document().setContent(new RandomString(100).nextString()).setCreation_date(LocalDate.now());
    }

    private DocumentIndex generateDocumentIndex(Document document, Index index) {
        return new DocumentIndex().setDocument(document).setIndex(index).setWeight(0.5).setCount(1L);
    }
}
