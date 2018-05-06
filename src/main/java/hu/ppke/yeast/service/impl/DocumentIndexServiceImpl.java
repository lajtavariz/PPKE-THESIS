package hu.ppke.yeast.service.impl;

import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.DocumentIndex;
import hu.ppke.yeast.domain.Index;
import hu.ppke.yeast.repository.DocumentIndexRepository;
import hu.ppke.yeast.repository.DocumentIndexWeightRepository;
import hu.ppke.yeast.repository.DocumentRepository;
import hu.ppke.yeast.repository.IndexRepository;
import hu.ppke.yeast.service.DocumentIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentIndexServiceImpl implements DocumentIndexService {

    private final DocumentIndexRepository docIndexRepository;
    private final DocumentRepository documentRepository;
    private final IndexRepository indexRepository;
    private final DocumentIndexWeightRepository documentIndexWeightRepository;

    @Autowired
    public DocumentIndexServiceImpl(DocumentIndexRepository docIndexRepository,
                                    DocumentRepository documentRepository,
                                    IndexRepository indexRepository,
                                    DocumentIndexWeightRepository documentIndexWeightRepository) {
        this.docIndexRepository = docIndexRepository;
        this.documentRepository = documentRepository;
        this.indexRepository = indexRepository;
        this.documentIndexWeightRepository = documentIndexWeightRepository;
    }

    @Override
    public DocumentIndex save(Document document, Index index, long count) {
        DocumentIndex docIndex = new DocumentIndex().setDocument(document).setIndex(index).setCount(count);

        docIndexRepository.save(docIndex);

        document.getDocumentIndices().add(docIndex);
        index.getDocumentIndices().add(docIndex);

        documentRepository.save(document);
        indexRepository.save(index);

        return docIndex;
    }

    @Override
    public void clearDB() {
        documentIndexWeightRepository.deleteAll();
        docIndexRepository.deleteAll();
        documentRepository.deleteAll();
        indexRepository.deleteAll();
    }
}
