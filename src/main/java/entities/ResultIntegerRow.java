package entities;

import java.util.List;

public class ResultIntegerRow extends IntegerRow {
    private Integer result;

    public ResultIntegerRow(List<Integer> cells, Integer result) {
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
