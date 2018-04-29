package hu.ppke.yeast.processor;

import hu.ppke.yeast.domain.Index;
import hu.ppke.yeast.repository.DocumentRepository;
import hu.ppke.yeast.repository.IndexRepository;
import hu.ppke.yeast.service.dto.DocumentSearchResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static hu.ppke.yeast.calculator.WeightCalculator.calculateWeight;

@Component
public class QueryProcessor extends AbstractProcessor {

    private final DocumentRepository documentRepository;

    @Autowired
    public QueryProcessor(ResourceLoader resourceLoader,
                          IndexRepository indexRepository,
                          DocumentRepository documentRepository) {
        super(resourceLoader, indexRepository);
        this.documentRepository = documentRepository;
    }

    public List<DocumentSearchResultDTO> getRelevantDocuments(String query, int metric) {
        List<Double> queryWeights = calculateQueryWeights(getRawIndices(query));


        return null;
    }

    private List<Double> calculateQueryWeights(List<String> queryIndices) {
        List<Double> queryWeights = new ArrayList<>();
        List<Index> allIndices = indexRepository.findAll();
        int nrOfAllDocuments = documentRepository.findAll().size();

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


    private List<String> getRawIndices(String query) {
        return stemWords(filterWords(getWords(query)));
    }

}
