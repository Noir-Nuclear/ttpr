package serialization;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import entities.ResultDoubleRow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataConverter {
    Map deserializedData;
    List<String> lines;

    public DataConverter(String path) {
        if (path.isEmpty()) {
            TXTLoader loader = new TXTLoader("D:\\MyFolder\\Projects\\Java\\ttpr\\src\\main\\resources\\txt\\lab4.txt");
            lines = loader.lines;
        } else {
            Gson gson = new Gson();
            JsonReader reader = null;
            try {
                reader = new JsonReader(new FileReader(new File(getClass().getClassLoader().getResource(path).getFile())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            deserializedData = gson.fromJson(Objects.requireNonNull(reader), Map.class);
        }
    }

    public List<List<Double>> getPoints() {
        Integer size = Integer.parseInt(lines.get(0));
        return IntStream.range(1, size + 1).mapToObj(i ->
                Arrays.stream(lines.get(i).split(" ")).
                        map(val -> Double.parseDouble(val)).
                        collect(Collectors.toList())
                ).
                collect(Collectors.toList());
    }

    public List<ResultDoubleRow> getCenters() {
        Integer size = Integer.parseInt(lines.get(0));
        return IntStream.range(size + 1, lines.size()).mapToObj(i -> {
            String[] cells = lines.get(i).split(" ");
            return new ResultDoubleRow(
                    Double.parseDouble(cells[1]),
                    Double.parseDouble(cells[2]),
                    Double.parseDouble(cells[3]),
                    Integer.parseInt(cells[0]));
        }).collect(Collectors.toList());
    }

    public List<List<Double>> getDataFromJson() {
        return (List<List<Double>>) deserializedData.get("data");
    }

    public List<Integer> getResultsFromJson() {
        return (List<Integer>) ((List) deserializedData.get("result")).stream().map(value -> ((Double) value).intValue()).collect(Collectors.toList());
    }

    public List<List<Double>> loadC() {
        return (List<List<Double>>) deserializedData.get("C");
    }

    public List<List<Double>> loadS() {
        return (List<List<Double>>) deserializedData.get("S");
    }

    public List<Double> loadQ() {
        return (List<Double>) deserializedData.get("Q");

    }

    public List<Double> loadP() {
        return (List<Double>) deserializedData.get("P");
    }

    public List<Double> loadPoint() {
        return (List<Double>) deserializedData.get("point");

    }
}
