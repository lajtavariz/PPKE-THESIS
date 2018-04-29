package hu.ppke.yeast.calculator;

public class WeightCalculator {

    public static double calculateTF_IDF(Long frequency, int nrOfAllDocuments, Long nrOfDocumentsInWhichTheIndexOccurs) {

        return frequency * Math.log10(nrOfAllDocuments / nrOfDocumentsInWhichTheIndexOccurs);
    }
}
