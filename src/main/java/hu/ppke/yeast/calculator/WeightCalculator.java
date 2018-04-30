package hu.ppke.yeast.calculator;

import static hu.ppke.yeast.config.constants.Constants.DEFAULT_WEIGHT_METHOD;
import static hu.ppke.yeast.enums.WeightMethod.TF_IDF;

public class WeightCalculator {

    public static double calculateWeight(Long freq, int nrOfAllDocuments, Long docCountForIndex) {

        if (TF_IDF.equals(DEFAULT_WEIGHT_METHOD)) {
            return calculateTF_IDF(freq, nrOfAllDocuments, docCountForIndex);
        } else {
            throw new UnsupportedOperationException("Weight calculation for " + DEFAULT_WEIGHT_METHOD + " is not yet supported!");
        }
    }

    private static double calculateTF_IDF(Long frequency, int nrOfAllDocuments, Long docCountForIndex) {
        double nrOfAllDocumentsDouble = nrOfAllDocuments;
        double docCountForIndexDouble = docCountForIndex;

        double weight = frequency * Math.log10(nrOfAllDocumentsDouble / docCountForIndexDouble);


        return weight;
    }
}
