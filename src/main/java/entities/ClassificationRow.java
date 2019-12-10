package entities;

public class ClassificationRow {
    private Integer cell1;
    private Integer cell2;
    private Integer cell3;
    private Integer count;
    private Integer resultClass;
    private Integer level;

    public ClassificationRow(Integer cell1, Integer cell2, Integer cell3, Integer count, Integer resultClass, Integer level) {
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.cell3 = cell3;
        this.count = count;
        this.resultClass = resultClass;
        this.level = level;
    }

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
}
