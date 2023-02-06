package Ex4;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Constraints:
 * Existence of an abstraction to which values can be converted.
 * This abstraction provides operations to (1) wrap around values, so that they become the abstraction; (2) bind itself to functions,
 * so to establish sequences of functions; and (3) unwrap the value, so to examine the final result.
 * Larger problem is solved as a pipeline of functions bound together, with unwrapping happening at the end.
 * Particularly for The One style, the bind operation simply calls the given function, giving it the value that it holds, and holds on to the returned value.
 */

public class Ten {

    /**
     * Building my binding class
     */

    static class BindingClass {
        private Object object;

        public BindingClass(Object object) {
            this.object = object;
        }

        public BindingClass bind(Function<Object, Object> function) {
            object = function.apply(object);
            return this;
        }

        public void print() {
            System.out.println(object);
        }
    }

    private static final Function<Object, Object> buildFilePathFromArguments = (object) -> {
        final String[] args = (String[]) object;

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

        return path;
    };

    /**
     * this method accepts file path
     * reads it line by line
     * adds it to a list and returns it
     */
    private static final Function<Object, Object> readPrideAndPrejudice = (object) -> {
        final Path path = (Path) object;
        List<String> listOfPridePrejudiceWords = new ArrayList<>();

        File file = new File(path.toString());
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split("[^a-zA-Z0-9]+");
            listOfPridePrejudiceWords.addAll(Arrays.asList(words));
        }
        scanner.close();
        return listOfPridePrejudiceWords;
    };

    /**
     * this method takes list of words read from pride and prejudice file
     * reads stop words
     * also filter them and add to map
     * returns the map.
     */
    private static final Function<Object, Object> mapWordsToFilteredMap = (object) -> {
        List<String> listOfPridePrejudiceWords = (List<String>) object;
        List<String> stop_words = new ArrayList<>();
        Map<String, Integer> frequencies = new HashMap<>();

        /**
         * read stop words
         */
        File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\stop_words.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split(",");
            stop_words.addAll(Arrays.asList(words));
        }
        scanner.close();

        /**
         * filter words and add it to map based on stop words
         */
        frequencies = listOfPridePrejudiceWords.stream().map(String::toLowerCase).
                filter(word -> !stop_words.contains(word) && word.length() > 1).
                collect(Collectors.toMap(word -> word, word -> 1, (Integer::sum)));

        return frequencies;
    };

    /**
     * this method takes a Map as input
     * and returns the top 25 as list of map
     */

    private static Function<Object, Object> sortTheMapReturnTop25 = (object) -> {
        Map<String, Integer> frequencies = (Map<String, Integer>) object;

        /**
         * doing the reverse sort
         * to get Top 25 most repeated words
         */

        List<Map.Entry<String, Integer>> top25 = frequencies.entrySet().stream()
                .sorted((val1, val2) -> val2.getValue() - val1.getValue())
                .limit(25)
                .collect(Collectors.toList());

        return top25;
    };

    /**
     * this method takes in List of Map
     * then returns the formatted output string to print method.
     */
    private static final Function<Object, Object> formattedTop25Words = (object) -> {
        List<Map.Entry<String, Integer>> top25 = (List<Map.Entry<String, Integer>>) object;
        StringBuilder outputTop25 = new StringBuilder();

        top25.forEach(word -> outputTop25.append(word.getKey()).append(" - ").append(word.getValue()).append("\n"));

        return outputTop25.toString();
    };


    public static void main(String args[]) {
        new BindingClass(args)
                .bind(buildFilePathFromArguments)
                .bind(readPrideAndPrejudice)
                .bind(mapWordsToFilteredMap)
                .bind(sortTheMapReturnTop25)
                .bind(formattedTop25Words)
                .print();
    }
}
