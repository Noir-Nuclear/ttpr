package entities;

import java.util.List;

public class DoubleRow {
    private Double cell1;
    private Double cell2;
    private Double cell3;

    public DoubleRow(Double cell1, Double cell2, Double cell3) {
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.cell3 = cell3;
    }

    public DoubleRow() {

    }

    public DoubleRow(List<Double> cells) {
        cell1 = cells.get(0);
        cell2 = cells.get(1);
        cell3 = cells.get(2);
    }

    public List<Double> getCells() {
        return List.of(cell1, cell2, cell3);
    }

    public Double getCell1() {
        return cell1;
    }

    public void setCell1(Double cell1) {
        this.cell1 = cell1;
    }

    public Double getCell2() {
        return cell2;
    }

    public void setCell2(Double cell2) {
        this.cell2 = cell2;
    }

    public Double getCell3() {
        return cell3;
    }

    public void setCell3(Double cell3) {
        this.cell3 = cell3;
    }
}
