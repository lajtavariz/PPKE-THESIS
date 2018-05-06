package hu.ppke.yeast.service.dto.evaluation;

public class QueryStatistic {

    private int id;
    private double precision;
    private double recall;

    public int getId() {
        return id;
    }

    public QueryStatistic setId(int id) {
        this.id = id;
        return this;
    }

    public double getPrecision() {
        return precision;
    }

    public QueryStatistic setPrecision(double precision) {
        this.precision = precision;
        return this;
    }

    public double getRecall() {
        return recall;
    }

    public QueryStatistic setRecall(double recall) {
        this.recall = recall;
        return this;
    }
}
