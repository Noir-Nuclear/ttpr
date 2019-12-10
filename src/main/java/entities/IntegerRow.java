package entities;

import java.util.List;
import java.util.Objects;

public class IntegerRow {
    private Integer cell1;
    private Integer cell2;
    private Integer cell3;

    public Integer getCell1() {
        return cell1;
    }

    public void setCell1(Integer cell1) {
        this.cell1 = cell1;
    }

    public Integer getCell2() {
        return cell2;
    }

    public void setCell2(Integer cell2) {
        this.cell2 = cell2;
    }

    public Integer getCell3() {
        return cell3;
    }

    public void setCell3(Integer cell3) {
        this.cell3 = cell3;
    }

    public IntegerRow(List<Integer> cells) {
        cell1 = cells.get(0);
        cell2 = cells.get(1);
        cell3 = cells.get(2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerRow that = (IntegerRow) o;
        return Objects.equals(cell1, that.cell1) &&
                Objects.equals(cell2, that.cell2) &&
                Objects.equals(cell3, that.cell3);
    }

    @Override
    public int hashCode() {

        return Objects.hash(cell1, cell2, cell3);
    }
}
