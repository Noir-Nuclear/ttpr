package math;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MathExecutor {
    public List<List<Integer>> normalizeData(List<List<Double>> data, int countOfLevels, double epsilon, Integer offset) {
        List<List<Integer>> normalizedData = new ArrayList<>();
        data.forEach(dataColumn ->
                normalizedData.add(dataColumn.stream().map(value -> {
                            Double a = value - getExtremeValue(dataColumn, false);
                            Double b = (getExtremeValue(dataColumn, true) - getExtremeValue(dataColumn, false)) / countOfLevels + epsilon;
                            return Double.valueOf(a / b).intValue() + offset;
                        }
                ).collect(Collectors.toList()))
        );
        return normalizedData;
    }

    Double getExtremeValue(List<Double> data, boolean needMax) {
        return data.stream().max((d1, d2) -> needMax ? d1.compareTo(d2) : d2.compareTo(d1)).get();
    }

    public Double getY(double x, List<Rule> rules) {
        return rules.stream().max(Comparator.comparing(rule -> rule.calculateValue(x))).get().calculateValue(x);
    }

    public double integrate(DoubleUnaryOperator f, double a, double b) {
        int i;
        double result, h;

        result = 0;

        h = 10E-5;
        int n = (int)((b - a) / h);

        for(i = 0; i < n; i++)
        {
            result += f.applyAsDouble(a + h * (i + 0.5)); //Вычисляем в средней точке и добавляем в сумму
        }

        result *= h;

        return result;
    }

    public double getDistance(List<Double> point1, List<Double> point2) {
        return Math.sqrt(IntStream.range(0, point1.size()).mapToDouble(i -> Math.pow(point1.get(i) - point2.get(i), 2.0)).sum());
    }
}
