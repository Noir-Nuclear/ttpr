import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import math.MathData;
import math.MathExecutor;
import math.Rule;
import serialization.DataConverter;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleController implements Initializable {
    public ComboBox menu;
    public TextField probabilityField;
    public TextField posibilityField;
    public TableView classifictaionTable;
    public TableView probabilityTable;
    public AnchorPane variableInputs;
    public TextField variable1;
    public TextField variable2;
    public TextField variable3;
    public TextArea result;
    public Button calculateNaiveResult;
    public AnchorPane lab2Inputs;
    public AnchorPane lab2Tables;
    public AnchorPane commonTables;
    public AnchorPane lab1Controls;
    public AnchorPane lab3Graphic;
    @FXML
    public LineChart<Number, Number> graphic;
    @FXML
    public NumberAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    public TextArea alphaDescription;
    public TextArea lab3Result;
    public AnchorPane lab4TablesAndButton;
    public TableView distanceTable;
    public TableView pointsTable;
    public TableView centersTable;
    public Button reCalculateCenters;
    DataConverter converter;
    MathExecutor executor;
    private NumberFormat formatter = new DecimalFormat("#0.0000");
    @FXML
    public TableView sourceTable;
    @FXML
    public TableView normalTable;
    @FXML
    public TableView behaviorTable;
    List<List<Double>> sourceData;
    List<List<Integer>> normalData;
    List<List<Integer>> trainingRows;
    List<Integer> results;
    List<ResultDoubleRow> centers;
    List<List<Double>> points;
    Map<List<Integer>, Long> counts;
    Double epsilon = 0.000000001;
    Integer countOfLevels;
    MathData mathData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        executor = new MathExecutor();
        graphic.setCreateSymbols(false);
        graphic.getStyleClass().add("thick-chart");
        menu.setItems(FXCollections.observableArrayList(
                "Самостоятельная 1",
                "Самостоятельная 2",
                "Самостоятельная 3",
                "Самостоятельная 4"));
        menu.setValue("Самостоятельная 1");
        menu.setOnAction(event -> {
            if (menu.getValue().equals(menu.getItems().get(0))) {
                processFirst();
            } else if (menu.getValue().equals(menu.getItems().get(1))) {
                processSecond();
            } else if (menu.getValue().equals(menu.getItems().get(2))) {
                processThird();
            } else if (menu.getValue().equals(menu.getItems().get(3))) {
                processFourth();
            }
        });
        calculateNaiveResult.setOnAction(event -> calculateNaive());
        processFirst();
    }

    private void processFourth() {
        converter = new DataConverter("");
        points = converter.getPoints();
        centers = converter.getCenters();
        commonTables.setVisible(false);
        lab1Controls.setVisible(false);
        lab2Inputs.setVisible(false);
        lab2Tables.setVisible(false);
        lab3Graphic.setVisible(false);
        lab4TablesAndButton.setVisible(true);
        visualizePointTable();
        visualizeCentersTable();
        visualizeDistanceTable();
        reCalculateCenters.setOnAction(event -> {
            List<DistanceRow> distanceRows = distanceTable.getItems();
            List<ResultDoubleRow> newCenters = IntStream.range(0, centers.size()).mapToObj(i -> {
                List<DistanceRow> currentRows = distanceRows.stream().
                        filter(row -> row.getClassIndex().equals(centers.get(i).getResult())).collect(Collectors.toList());
                if (currentRows.isEmpty()) {
                    return new ResultDoubleRow(0.0, 0.0, 0.0, -1);
                }
                ResultDoubleRow newCenter = new ResultDoubleRow();
                newCenter.setResult(centers.get(i).getResult());
                currentRows.forEach(row -> {
                    newCenter.setCell1((newCenter.getCell1() != null ? newCenter.getCell1() : 0.0) + points.get(row.getIndex()).get(0));
                    newCenter.setCell2((newCenter.getCell2() != null ? newCenter.getCell2() : 0.0) + points.get(row.getIndex()).get(1));
                    newCenter.setCell3((newCenter.getCell3() != null ? newCenter.getCell3() : 0.0) + points.get(row.getIndex()).get(2));
                });
                newCenter.setCell1(newCenter.getCell1() / currentRows.size());
                newCenter.setCell2(newCenter.getCell2() / currentRows.size());
                newCenter.setCell3(newCenter.getCell3() / currentRows.size());
                return newCenter;
            }).collect(Collectors.toList());
            centers = newCenters;
            visualizeCentersTable();
            visualizeDistanceTable();
        });
    }

    private void processThird() {
        converter = new DataConverter("json/lab3.json");
        mathData = new MathData();
        mathData.point = converter.loadPoint();
        loadRules();
        commonTables.setVisible(false);
        lab1Controls.setVisible(false);
        lab2Inputs.setVisible(false);
        lab2Tables.setVisible(false);
        lab3Graphic.setVisible(true);
        lab4TablesAndButton.setVisible(false);
        visualizeGraphic();
        visualizeAlphasAndResults();
    }

    private void processSecond() {
        converter = new DataConverter("json/lab2.json");
        countOfLevels = 2;
        calculateNormalData(1);
        results = converter.getResultsFromJson();
        commonTables.setVisible(true);
        lab1Controls.setVisible(false);
        lab2Inputs.setVisible(true);
        lab2Tables.setVisible(true);
        lab3Graphic.setVisible(false);
        lab4TablesAndButton.setVisible(false);
        visualizeTables(true);
        visualizeClassificationTable();
        visualizeProbabilityTable();
    }

    private void processFirst() {
        converter = new DataConverter("json/lab1.json");
        commonTables.setVisible(true);
        lab1Controls.setVisible(true);
        lab2Inputs.setVisible(false);
        lab2Tables.setVisible(false);
        lab3Graphic.setVisible(false);
        lab4TablesAndButton.setVisible(false);
        countOfLevels = 3;
        calculateNormalData(0);
        visualizeTables(false);
        visualizeBehaviorTable();
        calculateProbabilityFuzzyMeasure();
        calculatePossibilityFuzzyMeasure();
    }

    private void visualizeDistanceTable() {
        distanceTable.setItems(FXCollections.observableArrayList(
                IntStream.range(0, points.size()).mapToObj(i -> {
                    AtomicReference<Double> min = new AtomicReference<>(Double.MAX_VALUE);
                    AtomicInteger minIndex = new AtomicInteger(1);
                    List<Double> row = centers.stream().map(center -> {
                        Double value = executor.getDistance(points.get(i), center.getCells());
                        if (value < min.get()) {
                            minIndex.set(center.getResult());
                            min.set(value);
                        }
                        return value;
                    }).collect(Collectors.toList());
                    return new DistanceRow(row.get(0), row.get(1), row.get(2), i, minIndex.get());
                }).collect(Collectors.toList())
        ));
        TableColumn<DistanceRow, Double> doubleColumn1 = new TableColumn<>("d1");
        TableColumn<DistanceRow, Double> doubleColumn2 = new TableColumn<>("d2");
        TableColumn<DistanceRow, Double> doubleColumn3 = new TableColumn<>("d3");
        TableColumn<DistanceRow, Double> j = new TableColumn<>("j");
        TableColumn<DistanceRow, Double> k = new TableColumn<>("k");
        doubleColumn1.setCellValueFactory(new PropertyValueFactory<>("cell1"));
        doubleColumn2.setCellValueFactory(new PropertyValueFactory<>("cell2"));
        doubleColumn3.setCellValueFactory(new PropertyValueFactory<>("cell3"));
        j.setCellValueFactory(new PropertyValueFactory<>("index"));
        k.setCellValueFactory(new PropertyValueFactory<>("classIndex"));
        distanceTable.getColumns().clear();
        distanceTable.getColumns().addAll(j, doubleColumn1, doubleColumn2, doubleColumn3, k);
    }

    private void visualizeCentersTable() {
        centersTable.setItems(FXCollections.observableArrayList(centers));
        TableColumn<ResultIntegerRow, Double> doubleColumn1 = new TableColumn<>("c1");
        TableColumn<ResultIntegerRow, Double> doubleColumn2 = new TableColumn<>("c2");
        TableColumn<ResultIntegerRow, Double> doubleColumn3 = new TableColumn<>("c3");
        TableColumn<ResultIntegerRow, Integer> k = new TableColumn<>("k");
        doubleColumn1.setCellValueFactory(new PropertyValueFactory<>("cell1"));
        doubleColumn2.setCellValueFactory(new PropertyValueFactory<>("cell2"));
        doubleColumn3.setCellValueFactory(new PropertyValueFactory<>("cell3"));
        k.setCellValueFactory(new PropertyValueFactory<>("result"));
        centersTable.getColumns().clear();
        centersTable.getColumns().addAll(k, doubleColumn1, doubleColumn2, doubleColumn3);
    }

    private void visualizePointTable() {
        pointsTable.setItems(FXCollections.observableArrayList(
                IntStream.range(0, points.size()).mapToObj(i -> new ResultDoubleRow(
                        points.get(i).get(0),
                        points.get(i).get(0),
                        points.get(i).get(0),
                        i + 1
                )).collect(Collectors.toList())));
        TableColumn<ClassificationRow, Double> doubleColumn1 = new TableColumn<>("x1");
        TableColumn<ClassificationRow, Double> doubleColumn2 = new TableColumn<>("x2");
        TableColumn<ClassificationRow, Double> doubleColumn3 = new TableColumn<>("x3");
        TableColumn<ClassificationRow, Double> result = new TableColumn<>("j");
        doubleColumn1.setCellValueFactory(new PropertyValueFactory<>("cell1"));
        doubleColumn2.setCellValueFactory(new PropertyValueFactory<>("cell2"));
        doubleColumn3.setCellValueFactory(new PropertyValueFactory<>("cell3"));
        result.setCellValueFactory(new PropertyValueFactory<>("result"));
        pointsTable.getColumns().clear();
        pointsTable.getColumns().addAll(doubleColumn1, doubleColumn2, doubleColumn3, result);
    }

    private void visualizeAlphasAndResults() {
        final String[] results = {"", ""};
        IntStream.range(0, mathData.rules.size()).forEach(i -> results[0] += "alpha " + i + " = " + formatter.format(mathData.rules.get(i).alpha) + "\n");
        double res1 = executor.integrate(x -> x * executor.getY(x, mathData.rules), 0, 8);
        double res2 = executor.integrate(x -> executor.getY(x, mathData.rules), 0, 8);
        results[1] = "Значение интеграла числителя - " + formatter.format(res1) + "\n" +
                "Значение интеграла знаменателя - " + formatter.format(res2) + "\n" +
                "Четкое значение переменной = " + formatter.format(res1) + " / " + formatter.format(res2) + " = " + formatter.format(res1 / res2);
        alphaDescription.setText(results[0]);
        lab3Result.setText(results[1]);
    }

    private void visualizeGraphic() {
        graphic.getData().clear();
        XYChart.Series series = new XYChart.Series();
        Double step = 0.01;
        Double x = 0.0;
        while (x < 8.0) {
            series.getData().add(new XYChart.Data(x, executor.getY(x, mathData.rules)));
            x += step;
        }
        graphic.getData().add(series);
    }

    private void loadRules() {
        List<List<Double>> C = converter.loadC();
        List<List<Double>> S = converter.loadS();
        List<Double> q = converter.loadQ();
        List<Double> p = converter.loadP();
        mathData.rules = IntStream.range(0, p.size()).mapToObj(i -> {
            List<Double> alphas = IntStream.range(0, mathData.point.size()).mapToObj(j ->
                    Math.exp(
                            -(Math.pow(
                                    (mathData.point.get(j) - C.get(i).get(j)) /
                                            S.get(i).get(j),
                                    2.0
                            ))
                    )).collect(Collectors.toList());
            return new Rule(q.get(i), p.get(i), alphas.stream().min(Double::compareTo).get());
        }).collect(Collectors.toList());
    }

    private void calculateNaive() {
        try {
            Double var1 = Double.parseDouble(variable1.getCharacters().toString());
            Double var2 = Double.parseDouble(variable2.getCharacters().toString());
            Double var3 = Double.parseDouble(variable3.getCharacters().toString());
//            Integer var1 = Integer.parseInt(variable1.getCharacters().toString());
//            Integer var2 = Integer.parseInt(variable2.getCharacters().toString());
//            Integer var3 = Integer.parseInt(variable3.getCharacters().toString());
            List<List<Double>> newData = new ArrayList<>(sourceData);
            newData.add(List.of(var1, var2, var3));
            List<List<Integer>> newNormal = executor.normalizeData(newData, countOfLevels, epsilon, 1);
//            if (var1 < 0 || var1 > countOfLevels ||
//                    var2 < 0 || var2 > countOfLevels ||
//                    var3 < 0 || var3 > countOfLevels) {
//                result.setText("Некорректные данные в блоке переменных");
//                return;
//            }
            final String[] resString = {""};
            results.stream().sorted().collect(Collectors.toSet()).forEach(classValue -> {
                Double probValue = 1.0;
                probValue *= findRow(newNormal.get(newNormal.size() - 1).get(0), classValue).getCell1();
                probValue *= findRow(newNormal.get(newNormal.size() - 1).get(1), classValue).getCell2();
                probValue *= findRow(newNormal.get(newNormal.size() - 1).get(2), classValue).getCell3();
                resString[0] += "q- " + classValue + ", p -" +
                        formatter.format(
                                probValue * findRow(newNormal.get(newNormal.size() - 1).get(2), classValue).
                                        getProbability()) + "\n";
            });
            result.setText(resString[0]);
        } catch (Exception e) {
            result.setText("Некорректные данные в блоке переменных");
        }
    }

    ProbabilityRow findRow(int level, int classValue) {
        return ((List<ProbabilityRow>) probabilityTable.getItems().stream().filter(row ->
                Objects.equals(((ProbabilityRow) row).getLevel(), level) && Objects.equals(((ProbabilityRow) row).getResultClass(), classValue)
        ).
                collect(Collectors.toList())).get(0);
    }

    private void visualizeClassificationTable() {
        classifictaionTable.setItems(generateRows(ClassificationRow.class));
        TableColumn<ClassificationRow, Integer> integerColumn1 = new TableColumn<>("cell1");
        TableColumn<ClassificationRow, Integer> integerColumn2 = new TableColumn<>("cell2");
        TableColumn<ClassificationRow, Integer> integerColumn3 = new TableColumn<>("cell3");
        TableColumn<ClassificationRow, Integer> count = new TableColumn<>("count");
        TableColumn<ClassificationRow, Integer> classField = new TableColumn<>("resultClass");
        TableColumn<ClassificationRow, Integer> level = new TableColumn<>("level");
        integerColumn1.setCellValueFactory(new PropertyValueFactory<>("cell1"));
        integerColumn2.setCellValueFactory(new PropertyValueFactory<>("cell2"));
        integerColumn3.setCellValueFactory(new PropertyValueFactory<>("cell3"));
        count.setCellValueFactory(new PropertyValueFactory<>("count"));
        classField.setCellValueFactory(new PropertyValueFactory<>("resultClass"));
        level.setCellValueFactory(new PropertyValueFactory<>("level"));
        classifictaionTable.getColumns().clear();
        classifictaionTable.getColumns().addAll(integerColumn1, integerColumn2, integerColumn3, count, classField, level);
    }

    private void visualizeProbabilityTable() {
        probabilityTable.setItems(generateRows(ProbabilityRow.class));
        TableColumn<ProbabilityRow, Double> doubleColumn1 = new TableColumn<>("cell1");
        TableColumn<ProbabilityRow, Double> doubleColumn2 = new TableColumn<>("cell2");
        TableColumn<ProbabilityRow, Double> doubleColumn3 = new TableColumn<>("cell3");
        TableColumn<ProbabilityRow, Double> probability = new TableColumn<>("probability");
        TableColumn<ProbabilityRow, Integer> classField = new TableColumn<>("resultClass");
        TableColumn<ProbabilityRow, Integer> level = new TableColumn<>("level");
        doubleColumn1.setCellValueFactory(new PropertyValueFactory<>("cell1"));
        doubleColumn2.setCellValueFactory(new PropertyValueFactory<>("cell2"));
        doubleColumn3.setCellValueFactory(new PropertyValueFactory<>("cell3"));
        probability.setCellValueFactory(new PropertyValueFactory<>("probability"));
        Callback<TableColumn<ProbabilityRow, Double>, TableCell<ProbabilityRow, Double>> doubleVisualing = tc -> new TableCell<>() {
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(price));
                }
            }
        };
        doubleColumn1.setCellFactory(doubleVisualing);
        doubleColumn2.setCellFactory(doubleVisualing);
        doubleColumn3.setCellFactory(doubleVisualing);
        probability.setCellFactory(doubleVisualing);
        classField.setCellValueFactory(new PropertyValueFactory<>("resultClass"));
        level.setCellValueFactory(new PropertyValueFactory<>("level"));
        probabilityTable.getColumns().clear();
        probabilityTable.getColumns().addAll(doubleColumn1, doubleColumn2, doubleColumn3, probability, classField, level);
    }

    private void calculatePossibilityFuzzyMeasure() {
        SortedList sortedByLevel = behaviorTable.getItems().sorted(Comparator.comparing(r -> ((BehaviorData) r).getPossibilityValue()));
        final double[] currentLevel = {0};
        double epsilon = 0.00001;
        final double[] possibilityFuzzyMeasure = {0.0};
        sortedByLevel.forEach(item -> {
            double possiblityValue = ((BehaviorData) item).getPossibilityValue();
            if (possiblityValue < currentLevel[0] + epsilon) {
                return;
            }
            long count = behaviorTable.getItems().stream().filter(item2 -> {
                BigDecimal possibilityValue2 = BigDecimal.valueOf(((BehaviorData) item2).getPossibilityValue());
                return possibilityValue2.compareTo(
                        BigDecimal.valueOf(possiblityValue)
                ) >= 0;
            }).count();
            possibilityFuzzyMeasure[0] += Math.log(count) * (possiblityValue - currentLevel[0]) / Math.log(2.0);
            currentLevel[0] = possiblityValue;
        });
        posibilityField.setText(formatter.format(possibilityFuzzyMeasure[0]));
    }

    private void calculateProbabilityFuzzyMeasure() {
        AtomicReference<Double> value = new AtomicReference<>(0.0);
        behaviorTable.getItems().forEach(row -> {
            double probabilityValue = ((BehaviorData) row).getProbabilityValue();
            value.updateAndGet(v -> v + -probabilityValue * (Math.log(probabilityValue) / Math.log(2.0)));
        });
        probabilityField.setText(formatter.format(value.get()));
    }

    private void visualizeBehaviorTable() {
        counts = trainingRows.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        List<BehaviorData> behaviorDataList = new ArrayList<>();
        Long maxCount = counts.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
        counts.values().forEach(count -> behaviorDataList.add(new BehaviorData(
                        Math.toIntExact(count),
                        Double.longBitsToDouble(count) / Double.longBitsToDouble(trainingRows.size()),
                        Double.longBitsToDouble(count) / Double.longBitsToDouble(maxCount))
                )
        );
        behaviorTable.setItems(FXCollections.observableArrayList(behaviorDataList));
        TableColumn<BehaviorData, Integer> intColumn1 = new TableColumn<>("number");
        TableColumn<BehaviorData, Double> doubleColumn2 = new TableColumn<>("probabilityValue");

        Callback<TableColumn<BehaviorData, Double>, TableCell<BehaviorData, Double>> doubleVisualing = tc -> new TableCell<>() {
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(price));
                }
            }
        };
        doubleColumn2.setCellFactory(doubleVisualing);
        TableColumn<BehaviorData, Double> doubleColumn3 = new TableColumn<>("possibilityValue");
        doubleColumn3.setCellFactory(doubleVisualing);
        intColumn1.setCellValueFactory(new PropertyValueFactory<>("number"));
        doubleColumn2.setCellValueFactory(new PropertyValueFactory<>("probabilityValue"));
        doubleColumn3.setCellValueFactory(new PropertyValueFactory<>("possibilityValue"));
        behaviorTable.getColumns().clear();
        behaviorTable.getColumns().addAll(intColumn1, doubleColumn2, doubleColumn3);
    }

    public void initializeColumns(boolean extended) {
        if (extended) {
            TableColumn<ResultDoubleRow, Double> doubleColumn1 = new TableColumn<>("cell1");
            TableColumn<ResultDoubleRow, Double> doubleColumn2 = new TableColumn<>("cell2");
            TableColumn<ResultDoubleRow, Double> doubleColumn3 = new TableColumn<>("cell3");
            doubleColumn1.setCellValueFactory(new PropertyValueFactory<>("cell1"));
            doubleColumn2.setCellValueFactory(new PropertyValueFactory<>("cell2"));
            doubleColumn3.setCellValueFactory(new PropertyValueFactory<>("cell3"));
            TableColumn<ResultIntegerRow, Integer> integerColumn1 = new TableColumn<>("cell1");
            TableColumn<ResultIntegerRow, Integer> integerColumn2 = new TableColumn<>("cell2");
            TableColumn<ResultIntegerRow, Integer> integerColumn3 = new TableColumn<>("cell3");
            integerColumn1.setCellValueFactory(new PropertyValueFactory<>("cell1"));
            integerColumn2.setCellValueFactory(new PropertyValueFactory<>("cell2"));
            integerColumn3.setCellValueFactory(new PropertyValueFactory<>("cell3"));
            TableColumn<ResultDoubleRow, Integer> resultDoubleColumn = new TableColumn<>("result");
            resultDoubleColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
            TableColumn<ResultIntegerRow, Integer> resultIntegerColumn = new TableColumn<>("result");
            resultIntegerColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
            sourceTable.getColumns().addAll(doubleColumn1, doubleColumn2, doubleColumn3, resultDoubleColumn);
            normalTable.getColumns().addAll(integerColumn1, integerColumn2, integerColumn3, resultIntegerColumn);
        } else {
            TableColumn<DoubleRow, Double> doubleColumn1 = new TableColumn<>("cell1");
            TableColumn<DoubleRow, Double> doubleColumn2 = new TableColumn<>("cell2");
            TableColumn<DoubleRow, Double> doubleColumn3 = new TableColumn<>("cell3");
            doubleColumn1.setCellValueFactory(new PropertyValueFactory<>("cell1"));
            doubleColumn2.setCellValueFactory(new PropertyValueFactory<>("cell2"));
            doubleColumn3.setCellValueFactory(new PropertyValueFactory<>("cell3"));
            TableColumn<IntegerRow, Integer> integerColumn1 = new TableColumn<>("cell1");
            TableColumn<IntegerRow, Integer> integerColumn2 = new TableColumn<>("cell2");
            TableColumn<IntegerRow, Integer> integerColumn3 = new TableColumn<>("cell3");
            integerColumn1.setCellValueFactory(new PropertyValueFactory<>("cell1"));
            integerColumn2.setCellValueFactory(new PropertyValueFactory<>("cell2"));
            integerColumn3.setCellValueFactory(new PropertyValueFactory<>("cell3"));
            sourceTable.getColumns().addAll(doubleColumn1, doubleColumn2, doubleColumn3);
            normalTable.getColumns().addAll(integerColumn1, integerColumn2, integerColumn3);
        }
    }

    public void visualizeTables(boolean extended) {
        sourceTable.getColumns().clear();
        normalTable.getColumns().clear();
        initializeColumns(extended);
        if (extended) {
            sourceTable.setItems(generateRows(ResultDoubleRow.class));
            normalTable.setItems(generateRows(ResultIntegerRow.class));
        } else {
            sourceTable.setItems(generateRows(DoubleRow.class));
            normalTable.setItems(generateRows(IntegerRow.class));
        }
    }

    List<List<Integer>> generateTrainingRows() {
        return IntStream.range(0, normalData.get(0).size())
                .mapToObj(i -> normalData.stream().map(l -> l.get(i)).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    List<List<Double>> generateSourceRows() {
        return IntStream.range(0, sourceData.get(0).size())
                .mapToObj(i -> sourceData.stream().map(l -> l.get(i)).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    ObservableList generateRows(Class className) {
        if (className.equals(IntegerRow.class)) {
            trainingRows = generateTrainingRows();
            return FXCollections.observableArrayList(trainingRows.stream().map(IntegerRow::new).collect(Collectors.toList()));
        } else if (className.equals(DoubleRow.class)) {
            return FXCollections.observableArrayList(generateSourceRows().
                    stream().map(DoubleRow::new
            ).collect(Collectors.toList()));
        } else if (className.equals(ResultIntegerRow.class)) {
            List<List<Integer>> rows = generateTrainingRows();
            List<ResultIntegerRow> tableRows = IntStream.range(0, results.size()).mapToObj(i -> new ResultIntegerRow(rows.get(i), results.get(i))).collect(Collectors.toList());
            return FXCollections.observableArrayList(tableRows);
        } else if (className.equals(ResultDoubleRow.class)) {
            List<List<Double>> rows = generateSourceRows();
            List<ResultDoubleRow> tableRows = IntStream.range(0, results.size()).mapToObj(i -> new ResultDoubleRow(rows.get(i), results.get(i))).collect(Collectors.toList());
            return FXCollections.observableArrayList(tableRows);
        } else if (className.equals(ClassificationRow.class)) {
            List<ResultIntegerRow> rows = normalTable.getItems();
            List<ClassificationRow> classificationRows = new ArrayList<>();
            results.stream().sorted().collect(Collectors.toSet()).forEach(value -> {
                List<ResultIntegerRow> classRows = rows.stream().filter(row -> row.getResult().equals(value)).collect(Collectors.toList());
                classificationRows.addAll(IntStream.range(1, countOfLevels + 1).
                        mapToObj(level ->
                                new ClassificationRow(
                                        (int) classRows.stream().filter(row -> row.getCell1().equals(level)).count(),
                                        (int) classRows.stream().filter(row -> row.getCell2().equals(level)).count(),
                                        (int) classRows.stream().filter(row -> row.getCell3().equals(level)).count(),
                                        classRows.size(), value, level)).
                        collect(Collectors.toList()));
            });
            return FXCollections.observableArrayList(classificationRows);
        } else if (className.equals(ProbabilityRow.class)) {
            List<ClassificationRow> rows = classifictaionTable.getItems();
            List<ProbabilityRow> probabilityRows = rows.stream().map(row -> new ProbabilityRow(
                    ((double) row.getCell1()) / row.getCount(),
                    ((double) row.getCell2()) / row.getCount(),
                    ((double) row.getCell3()) / row.getCount(),
                    ((double) row.getCount()) / results.size(),
                    row.getResultClass(),
                    row.getLevel())).collect(Collectors.toList());
            return FXCollections.observableArrayList(probabilityRows);
        }
        return null;
    }

    public void calculateNormalData(Integer offset) {
        sourceData = converter.getDataFromJson();
        normalData = executor.normalizeData(sourceData, countOfLevels, epsilon, offset);
    }
}
