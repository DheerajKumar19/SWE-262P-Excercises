import MainFramework.ExtractWords;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;


public class ExtractWordsNormal implements ExtractWords {
    public ExtractWordsNormal() {
    }

    @Override
    public List<String> extractWords(String filePath) throws IOException {

        //        asList(Files.lines(Paths.get("stop_words.txt")).map(String::valueOf).collect(Collectors.joining("")).split(",")).forEach(s -> {
//            stop_words.add(s);
//        });

        List<String> finalStop_words = Files.lines(Paths.get("stop_words.txt"))
                .flatMap(line1 -> Arrays.stream(line1.split(",")))
                .collect(Collectors.toList());

        //        List<String> words = asList(Files.lines(Paths.get(filePath)).map(String::valueOf).collect(Collectors.joining(" ")).split("[^a-zA-Z0-9]+"));
//        for (String s : words) {
//            if (!stop_words.contains(s.toLowerCase()) && s.length() >= 2) result.add(s.toLowerCase());
//        }

        return Files.lines(Paths.get(filePath)).flatMap(line -> Arrays.stream(line.split("[^a-zA-Z0-9]+")))
                .map(String::toLowerCase)
                .filter(x -> !finalStop_words.contains(x) && x.length() > 1).collect(Collectors.toList());
    }
}
