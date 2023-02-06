package Ex4;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Variation of the candy factory style, with the following additional constraints:
 * Each function takes an additional parameter, usually the last, which is another function
 * That function parameter is applied at the end of the current function
 * That function parameter is given as input what would be the output of the current function
 * Larger problem is solved as a pipeline of functions, but where the next function to be applied is given as parameter to the current function
 **/
public class Nine {

    private static void buildFilePathFromArguments(String args[], BiConsumer<Path, BiConsumer> callback) {

        /**
         * process arguments
         * reference : SWE-264P Lab2
         */

        if (args.length != 1) {
            System.err.println("Please provide exactly ONE argument. Current: " + args.length + " args given " + args.toString());
            System.exit(1);
        }

        final Path path = Path.of(args[0]);
        /**
         * check for a file
         */
        if (!path.toFile().exists()) {
            System.err.println(path + " does not exist.");
            System.exit(1);
        }

        callback.accept(path, Nine::frequencyEstimate);

    }

    /**
     * @param path     from arguments
     * @param callback this is next function -> frequencyEstimate
     * @throws FileNotFoundException
     */
    public static void readPrejudice(Path path, BiConsumer<List<String>, BiConsumer> callback) {
        File file = new File(path.toString());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<String> words = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] w = line.split("[^a-zA-Z0-9]+");
            words.addAll(Arrays.asList(w));
        }
        scanner.close();

        /**
         * calls next function
         */

        callback.accept(words, Nine::reverseSort);
    }

    /**
     * this is a type cast instead of parsing the whole args into the function
     * @param o it is the current parameter to the function
     * @param o1 it is the parameter to call back
     */
    private static void frequencyEstimate(Object o, Object o1) {
        frequencyEstimate((List<String>) o, (BiConsumer<Map<String, Integer>, Consumer>) o1);
    }

    /**
     * @param words       from pride and prejudice file
     * @param callback this is the next stage it sends the map to
     */
    private static void frequencyEstimate(List<String> words, BiConsumer<Map<String, Integer>, Consumer> callback) {

        File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\stop_words.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<String> stop_words = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] stopwords = line.split(",");
            stop_words.addAll(Arrays.asList(stopwords));
        }
        scanner.close();

        Map<String, Integer> frequencies = new HashMap<>();
        for (String word : words) {
            String w = word.toLowerCase();
            if (!stop_words.contains(w) && w.length() >= 2) {
                if (frequencies.containsKey(w)) {
                    frequencies.put(w, frequencies.get(w) + 1);
                } else frequencies.put(w, 1);
            }
        }

        callback.accept(frequencies, Nine::printTop25);
    }

    /**
     * this is a type cast instead of parsing the whole args into the function
     * @param o it is the current parameter to the function
     * @param o1 it is the parameter to call back
     */
    private static void reverseSort(Object o, Object o1) {
        reverseSort((Map<String, Integer>) o, (Consumer<List<Map.Entry<String, Integer>>>) o1);
    }


    /**
     * @param frequencies a Map from previous
     * @param function       sends map to print function
     */
    private static void reverseSort(Map<String, Integer> frequencies, Consumer<List<Map.Entry<String, Integer>>> function) {
        List<Map.Entry<String, Integer>> top25 = frequencies.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(25)
                .collect(Collectors.toList());

        function.accept(top25);
    }

    /**
     * this is a type cast instead of parsing the whole args into the function
     * @param o it is the current parameter to the function
     */
    private static void printTop25(Object o) {
        printTop25((List<Map.Entry<String, Integer>>) o);
    }

    // void return type to define null function
    private static void printTop25(List<Map.Entry<String, Integer>> top25) {
        top25.stream().forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
    }

    public static void main(String args[]) throws FileNotFoundException {
        buildFilePathFromArguments(args, Nine::readPrejudice);
//        readPrejudice(args[0], Nine::frequencyEstimate);
    }
}
