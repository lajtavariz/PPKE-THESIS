package hu.ppke.yeast.web.rest;

import hu.ppke.yeast.YeastApp;
import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.DocumentIndex;
import hu.ppke.yeast.domain.Index;
import hu.ppke.yeast.repository.DocumentRepository;
import hu.ppke.yeast.repository.IndexRepository;
import hu.ppke.yeast.service.DocumentService;
import hu.ppke.yeast.service.dto.DocumentDTO;
import hu.ppke.yeast.service.mapper.DocumentMapper;
import hu.ppke.yeast.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static hu.ppke.yeast.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DocumentResource REST controller.
 *
 * @see DocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = YeastApp.class)
public class DocumentResourceIntTest {

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";
    private static final String CONTENT1_FOR_TXT_PROCESSING = "The quick 8brown {fox} jumped over the lazy dog,999 then the dog and the fox eat the rabbit:).";
    private static final String CONTENT2_FOR_TXT_PROCESSING = "Foxes are red and dogs are white";

    private static final String QUICK = "quick";
    private static final String BROWN = "brown";
    private static final String FOX = "fox";
    private static final String JUMP = "jump";
    private static final String LAZI = "lazi";
    private static final String DOG = "dog";
    private static final String EAT = "eat";
    private static final String RABBIT = "rabbit";
    private static final String OVER = "over";
    private static final String RED = "red";
    private static final String WHITE = "white";
    private static final List<String> INDICES_FOR_CONTENT1 = Arrays.asList(QUICK, BROWN, FOX, JUMP, LAZI, DOG, EAT, RABBIT, OVER);


    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDocumentMockMvc;

    private Document document;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DocumentResource documentResource = new DocumentResource(documentService);
        this.restDocumentMockMvc = MockMvcBuilders.standaloneSetup(documentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .setCreation_date(DEFAULT_CREATION_DATE)
            .setContent(DEFAULT_CONTENT);
        return document;
    }

    @Before
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    public void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document
        doPostRequestAndValidateResponse(document, HttpStatus.CREATED);

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getCreation_date()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDocument.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createDocument_persistedIndicesAreOK() throws Exception {
        // Create the Document
        DocumentDTO responseDocumentDTO = doPostRequestAndValidateResponse(new Document()
            .setCreation_date(DEFAULT_CREATION_DATE)
            .setContent(CONTENT1_FOR_TXT_PROCESSING), HttpStatus.CREATED);

        // Get the saved Document
        Document persistedDocument = documentRepository.getOne(responseDocumentDTO.getId());

        // Validate document indices
        Set<DocumentIndex> documentIndices = persistedDocument.getDocumentIndices();
        assertThat(documentIndices).hasSize(9);

        validateDocumentIndices(documentIndices, INDICES_FOR_CONTENT1);
    }

    @Test
    @Transactional
    public void createDocuments_persistedIndicesAreOK() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Documents
        DocumentDTO responseDocumentDTO1 = doPostRequestAndValidateResponse(new Document()
            .setCreation_date(DEFAULT_CREATION_DATE)
            .setContent(CONTENT1_FOR_TXT_PROCESSING), HttpStatus.CREATED);

        DocumentDTO responseDocumentDTO2 = doPostRequestAndValidateResponse(new Document()
            .setCreation_date(DEFAULT_CREATION_DATE)
            .setContent(CONTENT2_FOR_TXT_PROCESSING), HttpStatus.CREATED);

        // Validate the Document in the database
        Document persistedDocument1 = documentRepository.getOne(responseDocumentDTO1.getId());
        Document persistedDocument2 = documentRepository.getOne(responseDocumentDTO2.getId());

        // Validate document indices for Document1
        Set<DocumentIndex> documentIndices = persistedDocument1.getDocumentIndices();
        assertThat(documentIndices).hasSize(9);
        validateDocumentIndices(documentIndices, INDICES_FOR_CONTENT1);

        validateDocumentIndexForIndex(documentIndices, DOG, 2L);
        validateDocumentIndexForIndex(documentIndices, FOX, 2L);

        // Validate document indices for Document2
        documentIndices = persistedDocument2.getDocumentIndices();
        assertThat(documentIndices).hasSize(4);
        validateDocumentIndices(documentIndices, Arrays.asList(FOX, RED, DOG, WHITE));

        validateDocumentIndexForIndex(documentIndices, DOG, 1L);
        validateDocumentIndexForIndex(documentIndices, FOX, 1L);

        // Validate all persisted indices
        List<Index> allIndices = indexRepository.findAll();

        Map<String, Long> indexNameToTotalCount = new HashMap<>();
        indexNameToTotalCount.put(QUICK, 1L);
        indexNameToTotalCount.put(BROWN, 1L);
        indexNameToTotalCount.put(FOX, 3L);
        indexNameToTotalCount.put(JUMP, 1L);
        indexNameToTotalCount.put(OVER, 1L);
        indexNameToTotalCount.put(LAZI, 1L);
        indexNameToTotalCount.put(DOG, 3L);
        indexNameToTotalCount.put(EAT, 1L);
        indexNameToTotalCount.put(RABBIT, 1L);
        indexNameToTotalCount.put(RED, 1L);
        indexNameToTotalCount.put(WHITE, 1L);

        validateAllIndices(allIndices, indexNameToTotalCount, 11);
    }

    private void validateDocumentIndices(Set<DocumentIndex> documentIndices, List<String> expectedIndices) {
        assertThat(documentIndices).hasSize(expectedIndices.size());

        for (String expectedIndex : expectedIndices) {
            assertThat(documentIndices.stream().filter(p -> p
                .getIndex()
                .getName()
                .equals(expectedIndex)).collect(Collectors.toList())).hasSize(1);
        }
    }

    private void validateDocumentIndexForIndex(Set<DocumentIndex> documentIndices, String indexName, long expectedCount) {
        Optional<DocumentIndex> docIndex = documentIndices.stream().filter(p -> p.getIndex().getName().equals(indexName)).findAny();
        assertThat(docIndex.isPresent()).isTrue();
        assertThat(docIndex.get().getCount()).isEqualTo(expectedCount);
    }

    private void validateAllIndices(List<Index> allIndices, Map<String, Long> indexNameToTotalCount, int expectedNrOfIndices) {
        assertThat(allIndices).hasSize(expectedNrOfIndices);

        for (Index index : allIndices) {
            assertThat(index.getTotal_count()).isEqualTo(indexNameToTotalCount.get(index.getName()));
        }
    }

    @Test
    @Transactional
    public void createDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // Create the Document with an existing ID
        document.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        doPostRequestAndValidateResponse(document, HttpStatus.BAD_REQUEST);

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreation_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setCreation_date(null);

        doPostRequestAndValidateResponse(document, HttpStatus.BAD_REQUEST);

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc.perform(get("/api/documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].creation_date").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.creation_date").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get("/api/documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findOne(document.getId());
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .setCreation_date(UPDATED_CREATION_DATE)
            .setContent(UPDATED_CONTENT);
        DocumentDTO documentDTO = documentMapper.toDto(updatedDocument);

        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getCreation_date()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDocument.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDocumentMockMvc.perform(put("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);
        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Get the document
        restDocumentMockMvc.perform(delete("/api/documents/{id}", document.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Document.class);
        Document document1 = new Document();
        document1.setId(1L);
        Document document2 = new Document();
        document2.setId(document1.getId());
        assertThat(document1).isEqualTo(document2);
        document2.setId(2L);
        assertThat(document1).isNotEqualTo(document2);
        document1.setId(null);
        assertThat(document1).isNotEqualTo(document2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentDTO.class);
        DocumentDTO documentDTO1 = new DocumentDTO();
        documentDTO1.setId(1L);
        DocumentDTO documentDTO2 = new DocumentDTO();
        assertThat(documentDTO1).isNotEqualTo(documentDTO2);
        documentDTO2.setId(documentDTO1.getId());
        assertThat(documentDTO1).isEqualTo(documentDTO2);
        documentDTO2.setId(2L);
        assertThat(documentDTO1).isNotEqualTo(documentDTO2);
        documentDTO1.setId(null);
        assertThat(documentDTO1).isNotEqualTo(documentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(documentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(documentMapper.fromId(null)).isNull();
    }

    private DocumentDTO doPostRequestAndValidateResponse(Document document, HttpStatus expectedStatus) throws Exception {
        String response = restDocumentMockMvc.perform(post("/api/documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(documentMapper.toDto(document))))
            .andExpect(status().is(expectedStatus.value())).andReturn().getResponse().getContentAsString();

        return TestUtil.convertJsonStringToObject(new DocumentDTO(), response);
    }
}
