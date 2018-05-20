package hu.ppke.yeast.calculator;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static hu.ppke.yeast.calculator.ClassicalSimilarityCalculator.calculateSimilarity;
import static hu.ppke.yeast.enumeration.SimiliarityMeasure.DICE;
import static hu.ppke.yeast.enumeration.SimiliarityMeasure.JACCARD;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassicalSimilarityCalculatorTest {

    private static List<Double> queryVec;
    private static List<Double> docVec;

    @BeforeClass
    public static void setUp() {
        queryVec = Arrays.asList(3.0, 2.0, 1.0, 5.0);
        docVec = Arrays.asList(5.0, 9.0, 2.0, 1.0);
    }

    @Test
    public void testDice() {

        double result = calculateSimilarity(queryVec, docVec, DICE);

        assertThat(result).isEqualTo(2.857142857142857);
    }

    @Test
    public void testJaccard() {

        double result = calculateSimilarity(queryVec, docVec, JACCARD);

        assertThat(result).isEqualTo(20.809248554913296);
    }
}
