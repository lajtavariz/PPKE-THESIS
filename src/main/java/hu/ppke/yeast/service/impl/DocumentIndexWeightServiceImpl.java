package hu.ppke.yeast.service.impl;

import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.DocumentIndexWeight;
import hu.ppke.yeast.repository.DocumentIndexWeightRepository;
import hu.ppke.yeast.repository.DocumentRepository;
import hu.ppke.yeast.service.DocumentIndexWeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class DocumentIndexWeightServiceImpl implements DocumentIndexWeightService {

    private final DocumentIndexWeightRepository docIndexWeightRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentIndexWeightServiceImpl(DocumentIndexWeightRepository docIndexWeightRepository,
                                          DocumentRepository documentRepository) {
        this.docIndexWeightRepository = docIndexWeightRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public void save(Document document, List<DocumentIndexWeight> docIndexWeights) {

        docIndexWeightRepository.save(docIndexWeights);

        document.setDocumentIndexWeights(new HashSet<>(docIndexWeights));

        documentRepository.save(document);
    }
}
