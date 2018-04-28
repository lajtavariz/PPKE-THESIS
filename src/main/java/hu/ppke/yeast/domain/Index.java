package hu.ppke.yeast.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Index.
 */
@Entity
@Table(name = "jhi_index")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Index implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Min(value = 0L)
    @Column(name = "document_count")
    private Long documentCount;

    @OneToMany(mappedBy = "index")
    private Set<DocumentIndex> documentIndices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Index setName(String name) {
        this.name = name;
        return this;
    }

    public Long getDocumentCount() {
        return documentCount;
    }

    public Index setDocumentCount(Long documentCount) {
        this.documentCount = documentCount;
        return this;
    }

    public Set<DocumentIndex> getDocumentIndices() {
        return documentIndices;
    }

    public void setDocumentIndices(Set<DocumentIndex> documentIndices) {
        this.documentIndices = documentIndices;
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
        Index index = (Index) o;
        if (index.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), index.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Index{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", documentCount=" + getDocumentCount() +
            "}";
    }
}
