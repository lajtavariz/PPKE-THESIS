package hu.ppke.yeast.generator;

import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.DocumentIndex;
import hu.ppke.yeast.domain.DocumentIndexWeight;
import hu.ppke.yeast.domain.Index;
import hu.ppke.yeast.repository.DocumentIndexWeightRepository;
import hu.ppke.yeast.repository.DocumentRepository;
import hu.ppke.yeast.repository.IndexRepository;
import hu.ppke.yeast.service.DocumentIndexWeightService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hu.ppke.yeast.calculator.WeightCalculator.calculateWeight;


/**
 * This class is responsible for generating the weight for each possible Document-Index pair
 */
@Component
@Transactional
public class WeightMatrixGenerator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final IndexRepository indexRepository;
    private final DocumentRepository documentRepository;
    private final DocumentIndexWeightRepository documentIndexWeightRepository;
    private final DocumentIndexWeightService documentIndexWeightService;

    private int nrOfAllDocuments;

    @Autowired
    public WeightMatrixGenerator(IndexRepository indexRepository,
                                 DocumentRepository documentRepository,
                                 DocumentIndexWeightRepository documentIndexWeightRepository,
                                 DocumentIndexWeightService documentIndexWeightService) {
        this.indexRepository = indexRepository;
        this.documentRepository = documentRepository;
        this.documentIndexWeightRepository = documentIndexWeightRepository;
        this.documentIndexWeightService = documentIndexWeightService;
    }

    public void calculateAndPersistWeights() {

        final List<Document> allDocuments = documentRepository.findAll();
        final List<Index> allIndices = indexRepository.findAll();
        documentIndexWeightRepository.deleteAll();
        nrOfAllDocuments = allDocuments.size();

        for (Document currentDoc : allDocuments) {

            log.info("Generating weights for Document " + currentDoc.getId());

            final List<DocumentIndex> documentIndices = new ArrayList<>(currentDoc.getDocumentIndices());
            final List<DocumentIndexWeight> weightsForOwnIndices =
                getWeightsForOwnIndices(documentIndices);
            final List<DocumentIndexWeight> weightsForOtherIndices =
                getWeightsForOtherIndices(getOtherIndices(documentIndices, allIndices), currentDoc);

            List<DocumentIndexWeight> allWeights = new ArrayList<>();
            allWeights.addAll(weightsForOwnIndices);
            allWeights.addAll(weightsForOtherIndices);

            documentIndexWeightService.save(currentDoc, allWeights);
        }
    }

    private List<DocumentIndexWeight> getWeightsForOwnIndices(final List<DocumentIndex> documentIndices) {

        List<DocumentIndexWeight> documentIndexWeights = new ArrayList<>();

        for (DocumentIndex documentIndex : documentIndices) {
            double weight = calculateWeight(documentIndex.getCount(), nrOfAllDocuments, documentIndex.getIndex().getDocumentCount());
            documentIndexWeights.add(new DocumentIndexWeight()
                .setDocument(documentIndex.getDocument())
                .setIndex(documentIndex.getIndex())
                .setWeight(weight));
        }
        return documentIndexWeights;
    }

    @SuppressWarnings("unchecked")
    private List<Index> getOtherIndices(List<DocumentIndex> documentIndices, List<Index> allIndices) {

        List<Index> indicesForDoc = documentIndices.stream()
            .map(DocumentIndex::getIndex)
            .collect(Collectors.toList());

        return (List<Index>) CollectionUtils.removeAll(allIndices, indicesForDoc);
    }

    private List<DocumentIndexWeight> getWeightsForOtherIndices(final List<Index> otherIndices, Document document) {

        List<DocumentIndexWeight> documentIndexWeights = new ArrayList<>();

        for (Index currentIndex : otherIndices) {
            documentIndexWeights.add(new DocumentIndexWeight()
                .setDocument(document)
                .setIndex(currentIndex)
                .setWeight(0.0));
        }
        return documentIndexWeights;
    }
}
