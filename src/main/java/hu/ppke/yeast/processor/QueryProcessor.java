package hu.ppke.yeast.processor;

import hu.ppke.yeast.domain.Index;
import hu.ppke.yeast.repository.IndexRepository;
import hu.ppke.yeast.service.dto.DocumentSearchResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryProcessor extends AbstractProcessor {

    @Autowired
    public QueryProcessor(ResourceLoader resourceLoader, IndexRepository indexRepository) {
        super(resourceLoader, indexRepository);
    }

    public List<DocumentSearchResultDTO> getRelevantDocuments(String query, int metric) {
        List<Double> queryWeights = calculateQueryWeights(getRawIndices(query));


        return null;
    }

    private List<Double> calculateQueryWeights(List<String> queryIndices) {
        List<Double> queryWeights = new ArrayList<>();
        List<Index> allIndices = indexRepository.findAll();

        for (Index currentIndex : allIndices) {
            if (queryIndices.contains(currentIndex.getName())) {

                //TODO calculate the weight
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
