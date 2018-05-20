package hu.ppke.yeast.enumeration;

public enum SimiliarityMeasure {

    COSINE, HYPERBOLIC, DOT_PRODUCT, DICE, JACCARD;

    public static SimiliarityMeasure getMeasure(int i) {
        switch (i) {
            case 0:
                return COSINE;
            case 1:
                return HYPERBOLIC;
            case 2:
                return DOT_PRODUCT;
            case 3:
                return DICE;
            case 4:
                return JACCARD;
            default:
                return null;
        }
    }
}
