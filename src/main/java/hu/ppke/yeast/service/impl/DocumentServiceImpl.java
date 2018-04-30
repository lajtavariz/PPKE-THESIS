package hu.ppke.yeast.service.impl;

import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.enums.SimiliarityMeasure;
import hu.ppke.yeast.generator.WeightMatrixGenerator;
import hu.ppke.yeast.processor.DocumentProcessor;
import hu.ppke.yeast.processor.QueryProcessor;
import hu.ppke.yeast.repository.DocumentRepository;
import hu.ppke.yeast.service.DocumentService;
import hu.ppke.yeast.service.dto.DocumentDTO;
import hu.ppke.yeast.service.dto.DocumentSearchResultDTO;
import hu.ppke.yeast.service.mapper.DocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Document.
 */
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentProcessor documentProcessor;
    private final QueryProcessor queryProcessor;
    private final WeightMatrixGenerator weightMatrixGenerator;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository,
                               DocumentMapper documentMapper,
                               DocumentProcessor documentProcessor,
                               QueryProcessor queryProcessor,
                               WeightMatrixGenerator weightMatrixGenerator) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.documentProcessor = documentProcessor;
        this.queryProcessor = queryProcessor;
        this.weightMatrixGenerator = weightMatrixGenerator;
    }

    @Override
    public DocumentDTO save(DocumentDTO documentDTO) {
        log.debug("Request to save Document : {}", documentDTO);
        Document document = documentMapper.toEntity(documentDTO);
        document = documentRepository.save(document);
        documentProcessor.processDocument(document);
        weightMatrixGenerator.calculateAndPersistWeights();

        return documentMapper.toDto(document);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Documents");
        return documentRepository.findAll(pageable)
            .map(documentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentDTO findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        Document document = documentRepository.findOne(id);
        return documentMapper.toDto(document);
    }

    @Override
    public List<DocumentSearchResultDTO> search(String query, int measure) {
        return queryProcessor.getRelevantDocuments(query, SimiliarityMeasure.getMeasure(measure));
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.delete(id);
    }
}
