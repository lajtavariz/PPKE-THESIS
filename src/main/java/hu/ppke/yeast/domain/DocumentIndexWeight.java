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

    @ManyToOne(optional = false)
    @JoinColumn(name = "document_id")
    @NotNull
    private Document document;

    @ManyToOne(optional = false)
    @JoinColumn(name = "index_id")
    @NotNull
    private Index index;

    @Column(name = "weight")
    private Double weight;

    public Long getId() {
        return id;
    }

    public Document getDocument() {
        return document;
    }

    public DocumentIndexWeight setDocument(Document document) {
        this.document = document;
        return this;
    }

    public Index getIndex() {
        return index;
    }

    public DocumentIndexWeight setIndex(Index index) {
        this.index = index;
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

        if (docIndWeight.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), docIndWeight.getId());
    }

    @Override
    public String toString() {
        return "DocumentIndexWeight{" +
            "id=" + getId() +
            ", weight=" + getWeight() +
            "}";
    }
}
