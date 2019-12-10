package entities;

public class BehaviorData {
    private Integer number;
    private Double probabilityValue;
    private Double possibilityValue;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getProbabilityValue() {
        return probabilityValue;
    }

    public void setProbabilityValue(Double probabilityValue) {
        this.probabilityValue = probabilityValue;
    }

    public Double getPossibilityValue() {
        return possibilityValue;
    }

    public void setPossibilityValue(Double possibilityValue) {
        this.possibilityValue = possibilityValue;
    }

    public BehaviorData(Integer number, Double probabilityValue, Double possibilityValue) {
        this.number = number;
        this.probabilityValue = probabilityValue;
        this.possibilityValue = possibilityValue;
    }
}
