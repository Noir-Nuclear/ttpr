package entities;

public class ProbabilityRow {
    private Double cell1;
    private Double cell2;
    private Double cell3;
    private Double probability;
    private Integer resultClass;
    private Integer level;

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

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Integer getResultClass() {
        return resultClass;
    }

    public void setResultClass(Integer resultClass) {
        this.resultClass = resultClass;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public ProbabilityRow(Double cell1, Double cell2, Double cell3, Double probability, Integer resultClass, Integer level) {
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.cell3 = cell3;
        this.probability = probability;
        this.resultClass = resultClass;
        this.level = level;
    }
}
