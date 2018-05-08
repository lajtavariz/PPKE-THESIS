package hu.ppke.yeast.calculator;

import hu.ppke.yeast.service.dto.DocumentDTO;
import hu.ppke.yeast.service.dto.DocumentSearchResultDTO;
import hu.ppke.yeast.service.dto.evaluation.ADIQuery;
import hu.ppke.yeast.service.dto.evaluation.RecallPrecision;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static hu.ppke.yeast.calculator.SimilarityEvaluationCalculator.calculateAverageRecallPrecisionList;
import static hu.ppke.yeast.calculator.SimilarityEvaluationCalculator.calculateRecallPrecisionList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class to test SimilarityEvaluationCalculator
 */
public class SimilarityEvaluationCalculatorTest {

    private static Map<ADIQuery, List<DocumentSearchResultDTO>> queryToResults = new HashMap<>();
    private static ADIQuery query1;
    private static ADIQuery query2;
    private static ADIQuery query3;

    @BeforeClass
    public static void setUpTestClass() {
        query1 = new ADIQuery().setQuery("First question").setId(1)
            .setRelevantDocuments(Arrays.asList(3L, 6L, 8L, 9L));
        query2 = new ADIQuery().setQuery("Second question").setId(2)
            .setRelevantDocuments(Arrays.asList(1L, 2L, 5L, 8L));
        query3 = new ADIQuery().setQuery("Second question").setId(2)
            .setRelevantDocuments(Arrays.asList(1L, 2L, 3L, 6L, 7L));

        List<DocumentSearchResultDTO> searchResultList = generateSearchResultList();

        queryToResults.put(query1, searchResultList);
        queryToResults.put(query2, searchResultList);
        queryToResults.put(query3, searchResultList);
    }

    private static List<DocumentSearchResultDTO> generateSearchResultList() {
        List<DocumentSearchResultDTO> results = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            results.add(new DocumentSearchResultDTO(new DocumentDTO((long) i * 10,
                LocalDate.now(),
                "",
                (long) i), 1.0 / (double) i));
        }

        return results;
    }

    @Test
    public void calculateRecallPrecisionList_query1_isOK() {
        List<RecallPrecision> expectedRPList = new ArrayList<>();
        expectedRPList.add(new RecallPrecision(10, 1.0 / 3.0));
        expectedRPList.add(new RecallPrecision(20, 1.0 / 3.0));
        expectedRPList.add(new RecallPrecision(30, 2.0 / 6.0));
        expectedRPList.add(new RecallPrecision(40, 2.0 / 6.0));
        expectedRPList.add(new RecallPrecision(50, 2.0 / 6.0));
        expectedRPList.add(new RecallPrecision(60, 3.0 / 8.0));
        expectedRPList.add(new RecallPrecision(70, 3.0 / 8.0));
        expectedRPList.add(new RecallPrecision(80, 4.0 / 9.0));
        expectedRPList.add(new RecallPrecision(90, 4.0 / 9.0));
        expectedRPList.add(new RecallPrecision(100, 4.0 / 9.0));

        List<RecallPrecision> actualRPList = calculateRecallPrecisionList(queryToResults.get(query1), query1);

        assertThat(actualRPList).isEqualTo(expectedRPList);
    }

    @Test
    public void calculateRecallPrecisionList_query2_isOK() {
        List<RecallPrecision> expectedRPList = new ArrayList<>();
        expectedRPList.add(new RecallPrecision(10, 1));
        expectedRPList.add(new RecallPrecision(20, 1));
        expectedRPList.add(new RecallPrecision(30, 1));
        expectedRPList.add(new RecallPrecision(40, 1));
        expectedRPList.add(new RecallPrecision(50, 1));
        expectedRPList.add(new RecallPrecision(60, 0.6));
        expectedRPList.add(new RecallPrecision(70, 0.6));
        expectedRPList.add(new RecallPrecision(80, 0.5));
        expectedRPList.add(new RecallPrecision(90, 0.5));
        expectedRPList.add(new RecallPrecision(100, 0.5));

        List<RecallPrecision> actualRPList = calculateRecallPrecisionList(queryToResults.get(query2), query2);

        assertThat(actualRPList).isEqualTo(expectedRPList);
    }

    @Test
    public void calculateRecallPrecisionList_query3_isOK() {
        List<RecallPrecision> expectedRPList = new ArrayList<>();
        expectedRPList.add(new RecallPrecision(10, 1));
        expectedRPList.add(new RecallPrecision(20, 1));
        expectedRPList.add(new RecallPrecision(30, 1));
        expectedRPList.add(new RecallPrecision(40, 1));
        expectedRPList.add(new RecallPrecision(50, 1));
        expectedRPList.add(new RecallPrecision(60, 1));
        expectedRPList.add(new RecallPrecision(70, 2.0 / 3.0));
        expectedRPList.add(new RecallPrecision(80, 2.0 / 3.0));
        expectedRPList.add(new RecallPrecision(90, 5.0 / 7.0));
        expectedRPList.add(new RecallPrecision(100, 5.0 / 7.0));

        List<RecallPrecision> actualRPList = calculateRecallPrecisionList(queryToResults.get(query3), query3);

        assertThat(actualRPList).isEqualTo(expectedRPList);
    }

    @Test
    public void alculateAverageRecallPrecisionList_query1_2_3_isOK() {
        List<RecallPrecision> expectedRPList = new ArrayList<>();
        expectedRPList.add(new RecallPrecision(10, (1.0 / 3.0 + 1 + 1) / 3));
        expectedRPList.add(new RecallPrecision(20, (1.0 / 3.0 + 1 + 1) / 3));
        expectedRPList.add(new RecallPrecision(30, (1.0 / 3.0 + 1 + 1) / 3));
        expectedRPList.add(new RecallPrecision(40, (1.0 / 3.0 + 1 + 1) / 3));
        expectedRPList.add(new RecallPrecision(50, (1.0 / 3.0 + 1 + 1) / 3));
        expectedRPList.add(new RecallPrecision(60, (3.0 / 8.0 + 0.6 + 1) / 3));
        expectedRPList.add(new RecallPrecision(70, (3.0 / 8.0 + 0.6 + 2.0 / 3.0) / 3));
        expectedRPList.add(new RecallPrecision(80, (4.0 / 9.0 + 0.5 + 2.0 / 3.0) / 3));
        expectedRPList.add(new RecallPrecision(90, (4.0 / 9.0 + 0.5 + 5.0 / 7.0) / 3));
        expectedRPList.add(new RecallPrecision(100, (4.0 / 9.0 + 0.5 + 5.0 / 7.0) / 3));

        List<List<RecallPrecision>> listOfRpLists = new ArrayList<>();
        listOfRpLists.add(calculateRecallPrecisionList(queryToResults.get(query1), query1));
        listOfRpLists.add(calculateRecallPrecisionList(queryToResults.get(query2), query2));
        listOfRpLists.add(calculateRecallPrecisionList(queryToResults.get(query3), query3));

        List<RecallPrecision> actualRPList = calculateAverageRecallPrecisionList(listOfRpLists);

        assertThat(actualRPList).isEqualTo(expectedRPList);
    }


}
