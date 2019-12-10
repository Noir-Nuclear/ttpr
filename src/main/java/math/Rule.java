package math;

import java.math.BigDecimal;

public class Rule {
    public Double mean;
    public Double dispertion;
    public Double xBegin;
    public Double xEnd;
    public Double alpha;

    public Rule(Double mean, Double dispertion, Double alpha) {
        this.mean = mean;
        this.dispertion = dispertion;
        this.alpha = alpha;
        xBegin = mean - dispertion;
        xEnd = mean + dispertion;
    }

    public Double calculateValue(double x) {
        double returnedValue = 0.0;
        if (new BigDecimal(x).compareTo(new BigDecimal(mean)) == 0) {
            returnedValue = 1.0;
        } else if ((x < xBegin) || (x > xEnd)) {
            returnedValue = 0.0;
        } else if (x < mean) {
            returnedValue = (x - xBegin) / dispertion;
        } else if (x > mean) {
            returnedValue = (xEnd - x) / dispertion;
        }
        return Math.min(returnedValue, alpha);
    }
}
