package hu.ppke.yeast.calculator;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

/**
 * This class is responsible for calculating the hyperbolic similarity
 */
public class HyperbolicSimilarityCalculator {

    public static double calculateEuclideanDistance(List<Double> queryWeights,
                                                    List<Double> documentWeights) {

        RealVector queryVec = new ArrayRealVector(queryWeights.stream().mapToDouble(d -> d).toArray());
        RealVector documentVec = new ArrayRealVector(documentWeights.stream().mapToDouble(d -> d).toArray());

        return queryVec.getDistance(documentVec);
    }

    public static double calculateHyperBolicMeasure(double euclideanDistance, double r) {
        return 1 / Math.log1p(((r + euclideanDistance) / (r - euclideanDistance)) * Math.E);
    }
}
