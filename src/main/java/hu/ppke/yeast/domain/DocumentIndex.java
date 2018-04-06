package hu.ppke.yeast.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DocumentIndex.
 */
@Entity
@Table(name = "document_index")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DocumentIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "count")
    private Long count;

    @ManyToOne(optional = false)
    @JoinColumn(name = "document_id")
    @NotNull
    private Document document;

    @ManyToOne(optional = false)
    @JoinColumn(name = "index_id")
    @NotNull
    private Index index;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public Double getWeight() {
        return weight;
    }

    public DocumentIndex setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public DocumentIndex setCount(Long count) {
        this.count = count;
        return this;
    }

    public Document getDocument() {
        return document;
    }

    public DocumentIndex setDocument(Document document) {
        this.document = document;
        return this;
    }

    public Index getIndex() {
        return index;
    }

    public DocumentIndex setIndex(Index index) {
        this.index = index;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentIndex documentIndex = (DocumentIndex) o;
        if (documentIndex.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documentIndex.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DocumentIndex{" +
            "id=" + getId() +
            ", weight=" + getWeight() +
            ", count=" + getCount() +
            "}";
    }
}
