package hu.ppke.yeast.service.dto.evaluation;

import java.util.ArrayList;
import java.util.List;

public class ADIQuery {
    private int id;
    private String query;
    private List<Long> relevantDocuments = new ArrayList<>();

    public void addRelevantDocument(Long id) {
        relevantDocuments.add(id);
    }

    public int getId() {
        return id;
    }

    public ADIQuery setId(int id) {
        this.id = id;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public ADIQuery setQuery(String query) {
        this.query = query;
        return this;
    }

    public List<Long> getRelevantDocuments() {
        return relevantDocuments;
    }
}
