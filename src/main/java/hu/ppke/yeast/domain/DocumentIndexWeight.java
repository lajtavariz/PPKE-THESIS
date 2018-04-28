package hu.ppke.yeast.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DocumentIndexWeight.
 */
@Entity
@Table(name = "document_index_weight")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentIndexWeight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "document_id")
    private Long documentId;

    @NotNull
    @Column(name = "index_id")
    private Long indexId;

    @Column(name = "weight")
    private Double weight;

    public Long getId() {
        return id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public DocumentIndexWeight setDocumentId(Long documentId) {
        this.documentId = documentId;
        return this;
    }

    public Long getIndexId() {
        return indexId;
    }

    public DocumentIndexWeight setIndexId(Long indexId) {
        this.indexId = indexId;
        return this;
    }

    public Double getWeight() {
        return weight;
    }

    public DocumentIndexWeight setWeight(Double weight) {
        this.weight = weight;
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
        DocumentIndexWeight docIndWeight = (DocumentIndexWeight) o;

        return Objects.equals(this.getDocumentId(), docIndWeight.getDocumentId())
            && Objects.equals(this.getIndexId(), docIndWeight.getIndexId())
            && Objects.equals(this.getWeight(), docIndWeight.getWeight());
    }

    @Override
    public String toString() {
        return "DocumentIndexWeight{" +
            "id=" + getId() +
            "documentId=" + getDocumentId() +
            ", indexId=" + getIndexId() +
            ", weight=" + getWeight() +
            "}";
    }
}
