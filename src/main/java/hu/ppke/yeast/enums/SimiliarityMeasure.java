package hu.ppke.yeast.enums;

public enum SimiliarityMeasure {

    COSINE, HYPERBOLIC;

    public static SimiliarityMeasure getMeasure(int i) {
        switch (i) {
            case 0:
                return COSINE;
            case 1:
                return HYPERBOLIC;
            default:
                return null;
        }
    }
}
