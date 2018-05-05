package hu.ppke.yeast.enumeration;

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
