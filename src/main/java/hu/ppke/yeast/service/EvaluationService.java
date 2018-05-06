package hu.ppke.yeast.service;

import hu.ppke.yeast.service.dto.EvaluationResultDTO;

/**
 * Service Interface for the similarity measure evaluation
 */
public interface EvaluationService {

    /**
     * @param measure the similarity measure to evaluate
     * @return
     */
    EvaluationResultDTO evaluate(int measure);

}
