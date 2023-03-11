

import MainFramework.ExtractWords;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExtractWordsZWords implements ExtractWords {
    @Override
    public List<String> extractWords(String filePath) throws IOException {
        List<String> stop_words = Files.lines(Paths.get("stop_words.txt"))
                .flatMap(line1 -> Arrays.stream(line1.split(",")))
                .collect(Collectors.toList());

        return Files.lines(Paths.get(filePath)).flatMap(line -> Arrays.stream(line.split("[^a-zA-Z0-9]+")))
                .map(String::toLowerCase)
                .filter(x -> !stop_words.contains(x) && x.length() > 1 && x.toLowerCase().indexOf('z') >= 0).collect(Collectors.toList());
    }
}
