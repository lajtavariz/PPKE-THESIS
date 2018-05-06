package hu.ppke.yeast.service.dto.evaluation;

import java.util.ArrayList;
import java.util.List;

public class EvaluationResultDTO {

    List<QueryStatistic> queryStatistics = new ArrayList<>();

    public void addQueryStatistic(QueryStatistic statistic) {
        queryStatistics.add(statistic);
    }

}
