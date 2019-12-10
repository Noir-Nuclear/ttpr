package entities;

import java.util.List;

public class ResultDoubleRow extends DoubleRow {
    private Integer result;

    public ResultDoubleRow() {
        super();
    }

    public ResultDoubleRow(Double cell1, Double cell2, Double cell3, Integer result) {
        super(cell1, cell2, cell3);
        this.result = result;
    }

    public ResultDoubleRow(List<Double> cells, Integer result) {
        super(cells);
        this.result = result;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
