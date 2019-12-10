package serialization;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TXTLoader {
    List<String> lines;

    public TXTLoader(String path) {
        lines = new ArrayList<>();
        try {
            Files.lines(Paths.get(path), StandardCharsets.UTF_8).forEach(line -> lines.add(line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
