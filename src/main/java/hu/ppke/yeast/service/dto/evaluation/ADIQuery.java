package hu.ppke.yeast.service.dto.evaluation;

public class ADIQuery {
    int id;
    String query;

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
}
