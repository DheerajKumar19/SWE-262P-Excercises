package Ex4;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <references>:
 * <a href="https://github.com/crista/exercises-in-programming-style/blob/180f942397637beda8442bcec7038e65f17486df/15-hollywood/tf-15.py">...</a>
 * https://stackoverflow.com/questions/19405421/what-is-a-callback-method-in-java-term-seems-to-be-used-loosely
 * https://www.geeksforgeeks.org/asynchronous-synchronous-callbacks-java/
 * </references>
 * <p>
 * Constraints:
 * Larger problem is decomposed into entities using some form of abstraction (objects, modules or similar)
 * The entities are never called on directly for actions
 * The entities provide interfaces for other entities to be able to register callbacks
 * At certain points of the computation, the entities call on the other entities that have registered for callbacks
 */
public class Fifteen {

    static class WordFrequencyFramework {
        public final List<Consumer<String>> loadEventHandlers = new ArrayList<>();
        public final List<Procedure> doWorkEventHandler = new ArrayList<>();
        public final List<Procedure> endEventHandler = new ArrayList<>();

        /**
         * adding handler to their respective list
         *
         * @param handler
         */

        public void registerForLoadEvent(Consumer<String> handler) {
            loadEventHandlers.add(handler);
        }

        public void registerForWorkEvent(Procedure handler) {
            doWorkEventHandler.add(handler);
        }

        public void registerForEndEvent(Procedure handler) {
            endEventHandler.add(handler);
        }

        /**
         * implement the run method
         */

        public void run(String path) {
            loadEventHandlers.forEach(event -> event.accept(path));
            doWorkEventHandler.forEach(Procedure::run);
            endEventHandler.forEach(Procedure::run);
        }
    }

    /**
     * reading stop words
     */

    static class LoadStopwords {
        private static final List<String> stop_words = new ArrayList<>();

        /**
         * this will register the list we formed to next method
         *
         * @param frequencyFramework
         */
        public LoadStopwords(WordFrequencyFramework frequencyFramework) {
            frequencyFramework.registerForLoadEvent(this::readStopWords);
        }

        private void readStopWords(String unUsedParameter) {
            File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\stop_words.txt");
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
            } catch (Exception e) {

            }


            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split(",");
                stop_words.addAll(Arrays.asList(words));
            }
            scanner.close();
        }

        public boolean isAStopWord(String w) {
            return stop_words.contains(w);
        }
    }

    /**
     * this class produces the words from pride and prejudice
     */
    static class ReadPridePrejudice {
        private Stream<String> lines;
        final private LoadStopwords filter;
        final private List<Consumer<String>> wordEventHandler = new ArrayList<>();

        /**
         * @param frequencyFramework -> to run
         * @param filter             -> to load stop words
         */
        public ReadPridePrejudice(WordFrequencyFramework frequencyFramework, LoadStopwords filter) {
            frequencyFramework.registerForLoadEvent(this::loadFilePath);
            frequencyFramework.registerForWorkEvent(this::fetchPridePrejudiceWords);
            this.filter = filter;
        }

        private void loadFilePath(String s) {
            try {
                lines = Files.lines(Path.of(s));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void fetchPridePrejudiceWords() {
            lines.forEach(line -> {
                        String words[] = line.split("[^a-zA-Z0-9]+");
                        for (String w : words) {
                            w = w.toLowerCase();
                            if (!filter.isAStopWord(w) && w.length() > 1) {
                                String finalW = w;
                                wordEventHandler.forEach(handler -> handler.accept(finalW));
                            }
                        }
                    }
            );

        }

        /**
         * Subscribe to this event to fetch the words list
         *
         * @param handler
         */
        public void registerForWordEvent(Consumer<String> handler) {
            this.wordEventHandler.add(handler);
        }

    }

    /**
     * Counting frequency and printing the top 25 words
     * this event is called after loading pride and prejudice contents
     *
     * @param
     */

    static class FrequencyCalculator {
        private Map<String, Integer> frequencies = new HashMap<>();

        /**
         * this is subscribed to pride and prejudice
         *
         * @param framework
         * @param pridePrejudice
         */
        FrequencyCalculator(WordFrequencyFramework framework, ReadPridePrejudice pridePrejudice) {
            pridePrejudice.registerForWordEvent(this::addToFrequencyMap);
            framework.registerForEndEvent(this::printTop25);
        }

        private void addToFrequencyMap(String s) {
            if (frequencies.containsKey(s)) frequencies.put(s, frequencies.get(s) + 1);
            else frequencies.put(s, 1);
        }

        private void printTop25() {
            List<Map.Entry<String, Integer>> top25 = frequencies.entrySet().stream()
                    .sorted((val1, val2) -> val2.getValue() - val1.getValue())
                    .limit(25)
                    .collect(Collectors.toList());

            top25.stream().forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
        }
    }

    /**
     * 15.2 in text
     * Words with z. Change the given example program so that it implements
     * an additional task: after printing out the list of 25 top words, it should
     * print out the number of non-stop words with the letter z. Additional
     * constraints: (i) no changes should be made to the existing classes; adding
     * new classes and more lines of code to the main function is allowed; (ii)
     * files should be read only once for both tasks.
     *
     * @param
     */

    private static final class WordsWithZ {
        private final Set<String> wordsWithZ = new HashSet<>();

        /**
         * this method needs the words from pride prejudice
         *
         * @param framework      to invoke run
         * @param pridePrejudice loads words after removing stop words
         */
        WordsWithZ(WordFrequencyFramework framework, ReadPridePrejudice pridePrejudice) {
            pridePrejudice.registerForWordEvent(this::counter);
            framework.registerForEndEvent(this::print);
        }

        private void counter(String w) {
            if (w.contains("z")) {
                wordsWithZ.add(w);
            }
        }

        public void print() {

            System.out.println("\n ====> This is a requirement for 15.2, Unique non-stop words with 'Z': " + wordsWithZ.size());
        }
    }

    public static void buildPathFromArguments(String args[]) {
        // process arguments
        if (args.length != 1) {
            System.err.println("Please provide exactly ONE argument. Current: " + args.length);
            System.exit(1);
        }

        final Path path = Path.of(args[0]);
        if (!path.toFile().exists()) {
            System.err.println(path + " does not exist.");
            System.exit(1);
        }
    }


    /**
     * Implicit-invocation style
     * one's output event is the input to other.
     * @param args file path for pride and prejudice file
     */
    public static void main(String args[]) {
        buildPathFromArguments(args);
        WordFrequencyFramework framework = new WordFrequencyFramework();
        LoadStopwords loadStopwords = new LoadStopwords(framework);
        ReadPridePrejudice readPridePrejudice = new ReadPridePrejudice(framework, loadStopwords);
        new FrequencyCalculator(framework, readPridePrejudice);

        /**
         * for 15.2
         */
        new WordsWithZ(framework, readPridePrejudice);
        /**
         * shows begining
         */
        framework.run(args[0]);
    }
}

interface Procedure {
    void run();
}