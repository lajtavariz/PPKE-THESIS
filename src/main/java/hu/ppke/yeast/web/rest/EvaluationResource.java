package hu.ppke.yeast.web.rest;

import com.codahale.metrics.annotation.Timed;
import hu.ppke.yeast.service.EvaluationService;
import hu.ppke.yeast.service.dto.evaluation.EvaluationResultDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * REST controller to evaluate different similarity measures
 */
@RestController
@RequestMapping("/api")
public class EvaluationResource {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final EvaluationService evaluationService;

    @Autowired
    public EvaluationResource(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    /**
     * GET  /evaluate?measure=x : get the results of the evaluation process
     *
     * @param measure the chosen measure
     * @return the ResponseEntity with status 200 (OK) and computed results
     */
    @RequestMapping(value = "/evaluate", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<EvaluationResultDTO> evaluate(@RequestParam int measure) {
        log.debug("REST request to get the results of the similarity measure evaluation");
        EvaluationResultDTO evaluationResultDTO = evaluationService.evaluate(measure);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(evaluationResultDTO));
    }


}
