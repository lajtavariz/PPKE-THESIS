package hu.ppke.yeast.service.dto;

public class DocumentSearchResultDTO extends DocumentDTO {

    private double similarityMeasure;

    public double getSimilarityMeasure() {
        return similarityMeasure;
    }

    public DocumentSearchResultDTO setSimilarityMeasure(double similarityMeasure) {
        this.similarityMeasure = similarityMeasure;
        return this;
    }
}
