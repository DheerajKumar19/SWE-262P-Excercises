package Ex3;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * References => <a href="https://stackoverflow.com/questions/5343689/java-reading-a-file-into-an-arraylist">...</a>
 * <a href="https://stackoverflow.com/questions/30016832/reading-file-from-path-of-uri">...</a>
 * <a href="https://www.baeldung.com/jvm-configure-stack-sizes">...</a>
 */

public class Eight {

    public static void main(String[] args) throws IOException {
        Map<String, Integer> frequencies;
        try {
            URI uri1 = ClassLoader.getSystemResource("stop_words.txt").toURI();
            URI uri2 = ClassLoader.getSystemResource("pride-and-prejudice.txt").toURI();

            List<String> stop_words = Files.lines(Paths.get(uri1))
                    .flatMap(line -> Arrays.stream(line.split(",")))
                    .collect(Collectors.toList());

            List<String> words = new ArrayList<>();
            getWords(new BufferedReader(new FileReader(new File(uri2))), stop_words, words);

            frequencies = words.stream().collect(Collectors.toMap(word -> word, word -> 1, (Integer::sum)));

        } catch (Exception e) {
            e.getMessage();
            return;
        }

        frequencies.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(25)
                .forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
    }

    private static void getWords(BufferedReader reader, List<String> stop_words, List<String> words) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            int r;
            try {
                r = reader.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (r == -1) break;

            char c = (char) (r);
            if (Character.isLetterOrDigit(c)) {
                stringBuilder.append(c);
            } else {
                String word = new String(stringBuilder).toLowerCase();
                if (!word.isEmpty() && !stop_words.contains(word) && word.length() >= 2)
                    words.add(word);
                getWords(reader, stop_words, words);
            }
        }
    }
}
