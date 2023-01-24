package Ex2;
//Constraints:
//        Larger problem decomposed in procedural abstractions
//        Larger problem solved as a sequence of commands, each corresponding to a procedure

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class CookBook5 {
    private static final List<String> stop_words = new ArrayList<>();
    private static List<String> words = new ArrayList<>();
    private static Map<String, Integer> frequencies = new HashMap<>();
    private static List<Map.Entry<String, Integer>> top25 = new ArrayList<>();

    public static void readStopWords() throws FileNotFoundException {
        File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\stop_words.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split(",");
            stop_words.addAll(Arrays.asList(words));
        }
        scanner.close();
    }

    public static void readPrejudice() throws FileNotFoundException {
        File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\pride-and-prejudice.txt");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] w = line.split("[^a-zA-Z0-9]+");
            words.addAll(Arrays.asList(w));
        }
        scanner.close();
    }

    private static void frequencyEstimate() {
        for (String word : words) {
            String w = word.toLowerCase();
            if (!stop_words.contains(w) && w.length() >= 2) {
                if (frequencies.containsKey(w)) {
                    frequencies.put(w, frequencies.get(w) + 1);
                } else frequencies.put(w, 1);
            }
        }
    }

    private static void reverseSort() {
        top25 = frequencies.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(25)
                .collect(Collectors.toList());
    }

    private static void printTop25() {
        top25.stream().forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
    }

    public static void main(String args[]) throws FileNotFoundException {
        readStopWords();
        readPrejudice();
        frequencyEstimate();
        reverseSort();
        printTop25();
    }

}
