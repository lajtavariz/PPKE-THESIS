package hu.ppke.yeast.service.dto.evaluation;

import java.util.Objects;

public class RecallPrecision {

    double recall;
    double precision;

    public RecallPrecision(double recall, double precision) {
        this.recall = recall;
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecallPrecision that = (RecallPrecision) o;
        return Double.compare(that.recall, recall) == 0 &&
            Double.compare(that.precision, precision) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(recall, precision);
    }
}
