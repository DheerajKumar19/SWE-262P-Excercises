package Ex1;

/*
 * @author Crista Lopes
 * Simple word frequency program
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class FrequencyWords {
    private static final List<String> stop_words = new ArrayList<>();
    private static Map<String, Integer> frequencies = new HashMap<>();

    public static void readStopWords() throws FileNotFoundException {
        File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\stop_words.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split(",");
            stop_words.addAll(Arrays.asList(words));
        }
    }

    public static void readPrejudice() throws FileNotFoundException {
        File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\pride-and-prejudice.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split("\\W+");

            for (String word : words) {
                String w = word.toLowerCase();
                if (!stop_words.contains(w) && w.length() >= 2) {
                    if (frequencies.containsKey(w)) {
                        frequencies.put(w, frequencies.get(w) + 1);
                    } else frequencies.put(w, 1);
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        readStopWords();
        readPrejudice();
        List<Map.Entry<String, Integer>> top25 = frequencies.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(25)
                .collect(Collectors.toList());

        top25.stream().forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
    }


}
