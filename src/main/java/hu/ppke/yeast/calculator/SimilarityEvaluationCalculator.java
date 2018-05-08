package hu.ppke.yeast.calculator;

import hu.ppke.yeast.service.dto.DocumentSearchResultDTO;
import hu.ppke.yeast.service.dto.evaluation.ADIQuery;
import hu.ppke.yeast.service.dto.evaluation.QueryStatistic;
import hu.ppke.yeast.service.dto.evaluation.RecallPrecision;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for calculating the precision and recall for a given query
 */
public class SimilarityEvaluationCalculator {

    public static QueryStatistic generateQueryStatistic(List<DocumentSearchResultDTO> searchResults, ADIQuery query) {
        double rA = searchResults.stream().filter(p -> query.getRelevantDocuments().contains(p.getEvaluationId())).count();
        double r = query.getRelevantDocuments().size();
        double a = searchResults.size();

        double recall = rA / r;
        double precision = rA / a;

        return new QueryStatistic().setId(query.getId()).setRecall(recall).setPrecision(precision);
    }

    public static List<RecallPrecision> calculateRecallPrecisionList(List<DocumentSearchResultDTO> searchResults, ADIQuery query) {
        List<RecallPrecision> availableRecallPrecisions = new ArrayList<>();
        List<RecallPrecision> interpolatedRecallPrecisions = new ArrayList<>();
        double nrOfRelevantDocumentsFound = 0;
        double nrOfRelevantDocuments = query.getRelevantDocuments().size();

        for (int i = 0; i < searchResults.size(); i++) {
            if (query.getRelevantDocuments().contains(searchResults.get(i).getEvaluationId())) {
                nrOfRelevantDocumentsFound++;
                double precision = nrOfRelevantDocumentsFound / (double) (i + 1);
                double recall = nrOfRelevantDocumentsFound / nrOfRelevantDocuments * 100.0;
                availableRecallPrecisions.add(new RecallPrecision(recall, precision));
            }
        }

        int j = 0;
        for (int i = 10; i <= 100; i = i + 10) {
            if (availableRecallPrecisions.size() == 0) {
                interpolatedRecallPrecisions.add(new RecallPrecision(i, 0.0));
            } else {
                if (i <= availableRecallPrecisions.get(j).getRecall() || (j + 1 == availableRecallPrecisions.size())) {
                    interpolatedRecallPrecisions.add(new RecallPrecision(i, availableRecallPrecisions.get(j).getPrecision()));
                } else {
                    j++;
                    interpolatedRecallPrecisions.add(new RecallPrecision(i, availableRecallPrecisions.get(j).getPrecision()));
                }
            }
        }

        return interpolatedRecallPrecisions;
    }

    public static List<RecallPrecision> calculateAverageRecallPrecisionList(List<List<RecallPrecision>> allRecallPrecisionLists) {
        List<RecallPrecision> avgRecallPrecisions = new ArrayList<>();

        for (int i = 10; i <= 100; i = i + 10) {
            final int currentRecall = i;
            double avgPrecision = allRecallPrecisionLists.stream()
                .flatMap(List::stream)
                .filter(p -> p.getRecall() == currentRecall)
                .mapToDouble(RecallPrecision::getPrecision)
                .average()
                .orElse(Double.NaN);

            avgRecallPrecisions.add(new RecallPrecision(currentRecall, avgPrecision));
        }

        return avgRecallPrecisions;
    }
}
