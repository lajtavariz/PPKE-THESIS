package hu.ppke.yeast.calculator;

import hu.ppke.yeast.enumeration.SimiliarityMeasure;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

import static hu.ppke.yeast.enumeration.SimiliarityMeasure.*;

/**
 * This class is responsible for calculating the classical similarities
 */
public class ClassicalSimilarityCalculator {

    public static double calculateSimilarity(List<Double> queryWeights,
                                             List<Double> documentWeights, SimiliarityMeasure measure) {

        RealVector queryVec = new ArrayRealVector(queryWeights.stream().mapToDouble(d -> d).toArray());
        RealVector documentVec = new ArrayRealVector(documentWeights.stream().mapToDouble(d -> d).toArray());

        if (COSINE.equals(measure)) {
            return calculateCosineMeasure(queryVec, documentVec);
        } else if (DOT_PRODUCT.equals(measure)) {
            return queryVec.dotProduct(documentVec);
        } else if (DICE.equals(measure)) {
            return calculateDice(queryVec, documentVec);
        } else if (JACCARD.equals(measure)) {
            return calculateJaccard(queryVec, documentVec);
        } else {
            throw new UnsupportedOperationException("Similarity measure calculation for " +
                measure + " measure is not yet supported!");
        }
    }

    private static double calculateCosineMeasure(RealVector queryVec, RealVector documentVec) {

        double numerator = queryVec.dotProduct(documentVec);
        double denominator = Math.sqrt(queryVec.dotProduct(queryVec)) * Math.sqrt(documentVec.dotProduct(documentVec));

        if (denominator == 0.0) {
            return 0.0;
        } else {
            return numerator / denominator;
        }
    }

    private static double calculateDice(RealVector queryVec, RealVector documentVec) {

        double numerator = 2 * queryVec.dotProduct(documentVec);
        double denominator = sumCoordinates(queryVec.add(documentVec));

        if (denominator == 0.0) {
            return 0.0;
        } else {
            return numerator / denominator;
        }
    }

    private static double calculateJaccard(RealVector queryVec, RealVector documentVec) {

        double numerator = queryVec.dotProduct(documentVec);
        double[] queryArray = queryVec.toArray();
        double[] docArray = documentVec.toArray();
        double[] coordinatesAfterCalc = new double[queryArray.length];

        for (int i = 0; i < queryArray.length; i++) {
            coordinatesAfterCalc[i] = ((queryArray[i] + docArray[i]) / (2 * queryArray[i] * docArray[i]));
        }

        double denominator = sumCoordinates(coordinatesAfterCalc);

        if (denominator == 0.0) {
            return 0.0;
        } else {
            return numerator / denominator;
        }
    }

    private static double sumCoordinates(RealVector vector) {
        return sumCoordinates(vector.toArray());
    }

    private static double sumCoordinates(double[] coordinates) {
        double sum = 0;

        for (double i : coordinates) {
            sum += i;
        }

        return sum;
    }

}
