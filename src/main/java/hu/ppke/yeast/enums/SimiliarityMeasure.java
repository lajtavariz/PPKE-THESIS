package hu.ppke.yeast.enums;

public enum SimiliarityMeasure {

    COSINE;

    public static SimiliarityMeasure getMeasure(int i) {
        switch (i) {
            case 0:
                return COSINE;
            default:
                return null;
        }
    }
}
