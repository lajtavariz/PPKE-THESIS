package hu.ppke.yeast.service.dto;

import hu.ppke.yeast.service.dto.evaluation.QueryStatistic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for the evaluation results
 */
public class EvaluationResultDTO implements Serializable {

    private List<QueryStatistic> queryStatistics = new ArrayList<>();

    public void addQueryStatistic(QueryStatistic statistic) {
        queryStatistics.add(statistic);
    }

    public List<QueryStatistic> getQueryStatistics() {
        return queryStatistics;
    }

    public EvaluationResultDTO setQueryStatistics(List<QueryStatistic> queryStatistics) {
        this.queryStatistics = queryStatistics;
        return this;
    }
}
