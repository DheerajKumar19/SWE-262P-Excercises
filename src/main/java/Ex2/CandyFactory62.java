package Ex2;

//Constraints:
//
//        Larger problem decomposed in functional abstractions. Functions, according to Mathematics, are relations from inputs to outputs.
//        Larger problem solved as a pipeline of function applications

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class CandyFactory62 {
    public static List<String> readStopWords() throws FileNotFoundException {
        File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\stop_words.txt");
        Scanner scanner = new Scanner(file);
        List<String> stop_words = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split(",");
            stop_words.addAll(Arrays.asList(words));
        }
        scanner.close();
        return stop_words;
    }

    public static List<String> readPrejudice() throws FileNotFoundException {
        File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\pride-and-prejudice.txt");
        Scanner scanner = new Scanner(file);
        List<String> words = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] w = line.split("[^a-zA-Z0-9]+");
            words.addAll(Arrays.asList(w));
        }
        scanner.close();
        return words;
    }

    private static Map<String, Integer> frequencyEstimate(List<String> words, List<String> stop_words) {
        Map<String, Integer> frequencies = new HashMap<>();
        for (String word : words) {
            String w = word.toLowerCase();
            if (!stop_words.contains(w) && w.length() >= 2) {
                if (frequencies.containsKey(w)) {
                    frequencies.put(w, frequencies.get(w) + 1);
                } else frequencies.put(w, 1);
            }
        }
        return frequencies;
    }

    private static List<Map.Entry<String, Integer>> reverseSort(Map<String, Integer> frequencies) {
        List<Map.Entry<String, Integer>> top25 = new ArrayList<>();
        top25 = frequencies.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(25)
                .collect(Collectors.toList());
        return top25;
    }

    private static void printTop25(List<Map.Entry<String, Integer>> top25) {
        top25.stream().forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
    }

    public static void main(String args[]) throws FileNotFoundException {
        printTop25(reverseSort(frequencyEstimate(readPrejudice(), readStopWords())));
    }
}
