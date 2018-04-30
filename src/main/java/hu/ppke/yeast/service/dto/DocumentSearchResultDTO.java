package hu.ppke.yeast.service.dto;

public class DocumentSearchResultDTO extends DocumentDTO {

    private double similarityMeasure;

    public DocumentSearchResultDTO(DocumentDTO documentDTO, double similarityMeasure) {
        super(documentDTO.getId(), documentDTO.getCreation_date(), documentDTO.getContent());
        this.similarityMeasure = similarityMeasure;
    }

    public double getSimilarityMeasure() {
        return similarityMeasure;
    }

    public DocumentSearchResultDTO setSimilarityMeasure(double similarityMeasure) {
        this.similarityMeasure = similarityMeasure;
        return this;
    }
}
