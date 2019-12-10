package entities;

public class DistanceRow {
    private Double cell1;
    private Double cell2;
    private Double cell3;
    private Integer index;
    private Integer classIndex;

    public DistanceRow(Double cell1, Double cell2, Double cell3, Integer index, Integer classIndex) {
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.cell3 = cell3;
        this.index = index;
        this.classIndex = classIndex;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(Integer classIndex) {
        this.classIndex = classIndex;
    }
}
