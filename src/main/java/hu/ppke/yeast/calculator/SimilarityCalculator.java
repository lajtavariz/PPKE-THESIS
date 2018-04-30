package hu.ppke.yeast.calculator;

import hu.ppke.yeast.enums.SimiliarityMeasure;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

import static hu.ppke.yeast.enums.SimiliarityMeasure.COSINE;

public class SimilarityCalculator {

    public static double calculateSimilarity(List<Double> queryWeights, List<Double> documentWeights, int measure) {
        RealVector queryVec = new ArrayRealVector(queryWeights.stream().mapToDouble(d -> d).toArray());
        RealVector documentVec = new ArrayRealVector(documentWeights.stream().mapToDouble(d -> d).toArray());

        if (COSINE.equals(SimiliarityMeasure.getMeasure(measure))) {
            return calculateCosineMeasure(queryVec, documentVec);
        } else {
            throw new UnsupportedOperationException("Similarity measure calculation with id " + measure + " is not yet supported!");
        }
    }

    private static double calculateCosineMeasure(RealVector queryVec, RealVector documentVec) {

        return queryVec.dotProduct(documentVec)
            / Math.sqrt(queryVec.dotProduct(queryVec)) * Math.sqrt(documentVec.dotProduct(documentVec));
    }
}
