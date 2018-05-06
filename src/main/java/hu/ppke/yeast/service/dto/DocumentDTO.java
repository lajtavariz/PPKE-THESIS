package hu.ppke.yeast.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Document entity.
 */
public class DocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate creation_date;

    private String content;

    private Long evaluationId;

    public DocumentDTO() {
    }

    DocumentDTO(Long id, LocalDate creation_date, String content) {
        this.id = id;
        this.creation_date = creation_date;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public DocumentDTO setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if(documentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + getId() +
            ", creation_date='" + getCreation_date() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
