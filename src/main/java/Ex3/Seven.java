package Ex3;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * References => <a href="https://stackoverflow.com/questions/5343689/java-reading-a-file-into-an-arraylist">...</a>
 */

public class Seven {
    private static final List<String> stop_words = new ArrayList<>();
    private static final List<String> prideAndPrejudice = new ArrayList<>();
    private static Map<String, Integer> frequencies = new HashMap<>();

    public static void main(String[] args) throws IOException {
        try {
            URI uri1 = ClassLoader.getSystemResource("stop_words.txt").toURI();
            URI uri2 = ClassLoader.getSystemResource("pride-and-prejudice.txt").toURI();
            Files.lines(Paths.get(uri1))
                    .forEach(line -> stop_words.addAll(Arrays.asList(line.split(","))));
            frequencies = Files.lines(Paths.get(uri2))
                    .flatMap(line -> Arrays.stream(line.split("[^a-zA-Z0-9]+")))
                    .map(String::toLowerCase)
                    .filter(x -> !stop_words.contains(x) && x.length() > 1)
                    .collect(Collectors.toMap(word -> word, word -> 1, (Integer::sum)));
        } catch (Exception e) {
            e.getMessage();
        }
        frequencies.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(25)
                .forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
    }
}
