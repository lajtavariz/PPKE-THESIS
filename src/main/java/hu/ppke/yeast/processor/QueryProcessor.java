package hu.ppke.yeast.processor;

import hu.ppke.yeast.domain.Document;
import hu.ppke.yeast.domain.DocumentIndexWeight;
import hu.ppke.yeast.domain.Index;
import hu.ppke.yeast.enums.SimiliarityMeasure;
import hu.ppke.yeast.repository.DocumentIndexWeightRepository;
import hu.ppke.yeast.repository.DocumentRepository;
import hu.ppke.yeast.repository.IndexRepository;
import hu.ppke.yeast.service.dto.DocumentSearchResultDTO;
import hu.ppke.yeast.service.mapper.DocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static hu.ppke.yeast.calculator.SimilarityCalculator.calculateSimilarity;
import static hu.ppke.yeast.calculator.WeightCalculator.calculateWeight;

/**
 * This class is responsible for giving back the relevant documents for a given query
 */
@Component
public class QueryProcessor extends AbstractProcessor {

    private final DocumentRepository documentRepository;
    private final DocumentIndexWeightRepository documentIndexWeightRepository;
    private final DocumentMapper documentMapper;

    @Autowired
    public QueryProcessor(ResourceLoader resourceLoader,
                          IndexRepository indexRepository,
                          DocumentRepository documentRepository,
                          DocumentIndexWeightRepository documentIndexWeightRepository,
                          DocumentMapper documentMapper) {
        super(resourceLoader, indexRepository);
        this.documentRepository = documentRepository;
        this.documentIndexWeightRepository = documentIndexWeightRepository;
        this.documentMapper = documentMapper;
    }

    public List<DocumentSearchResultDTO> getRelevantDocuments(String query, SimiliarityMeasure measure) {
        List<Document> documents = documentRepository.findAll();
        List<Double> queryWeights = calculateQueryWeights(getRawIndices(query), documents.size());

        return getRelevantDocuments(queryWeights, documents, measure);
    }

    private List<Double> calculateQueryWeights(List<String> queryIndices, int nrOfAllDocuments) {
        List<Double> queryWeights = new ArrayList<>();
        List<Index> allIndices = indexRepository.findAll();

        for (Index currentIndex : allIndices) {
            if (queryIndices.contains(currentIndex.getName())) {

                Long freq = queryIndices.stream().filter(p -> p.equals(currentIndex.getName())).count();
                queryWeights.add(calculateWeight(freq, nrOfAllDocuments, currentIndex.getDocumentCount()));

            } else {
                queryWeights.add(0.0);
            }
        }

        return queryWeights;
    }

    private List<DocumentSearchResultDTO> getRelevantDocuments(List<Double> queryWeights,
                                                               List<Document> documents,
                                                               SimiliarityMeasure measure) {

        List<DocumentSearchResultDTO> resultList = new ArrayList<>();

        for (Document currentDoc : documents) {
            List<Double> documentWeights = documentIndexWeightRepository
                .findByDocumentIdOrderByIndexIdAsc(currentDoc.getId())
                .stream().map(DocumentIndexWeight::getWeight).collect(Collectors.toList());

            double similarityMeasure = calculateSimilarity(queryWeights, documentWeights, measure);

            if (similarityMeasure != 0.0) {
                DocumentSearchResultDTO documentSearchResultDTO = (DocumentSearchResultDTO) documentMapper.toDto(currentDoc);
                resultList.add(documentSearchResultDTO.setSimilarityMeasure(similarityMeasure));
            }
        }

        return resultList
            .stream()
            .sorted((p1, p2) -> Double.compare(p2.getSimilarityMeasure(), p1.getSimilarityMeasure()))
            .collect(Collectors.toList());
    }

    private List<String> getRawIndices(String query) {
        return stemWords(filterWords(getWords(query)));
    }

}
