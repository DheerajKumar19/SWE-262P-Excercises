package thirtytwo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Main {

    static Map<String, Integer> frequencies = new HashMap<>();

    /**
     *
     * @param <L> String
     * @param <R> Integer for count
     */
    static class Pair<L, R> {
        public L left;
        public R right;

        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) {
        File mainText = new File("C:\\WinterQuarter-23\\262P-Project\\week8\\src\\main\\java\\org\\example\\pride-and-prejudice.txt");
        File stopWordsFile = new File("C:\\WinterQuarter-23\\262P-Project\\week8\\src\\main\\java\\org\\example\\stop_words.txt");

        /**
         * load stop words into a list
         */
        try {
            Scanner stop = new Scanner(stopWordsFile).useDelimiter(",\\s?");
            Set<String> stopWords = new HashSet<>();
            while (stop.hasNext()) {
                stopWords.add(stop.next());
            }


            /**
             * read line by line here lines are my data chunks
             */
            Scanner pnp = new Scanner(mainText);
            List<String> lines = new ArrayList<>();
            while (pnp.hasNext()) lines.add(pnp.nextLine());

            /**
             * ConcurrentLinkedDeque to avoid race condition
             */
            ConcurrentLinkedDeque<List<Pair<String, Integer>>> splitResults = new ConcurrentLinkedDeque<>();
            List<Thread> wordThreads = new ArrayList<>();

            /**
             * feeding 10 lines as chunks of data to my worker threads
             */
            for (int i = 0; i < lines.size(); i += 10) {
                List<String> currentLines = lines.subList(i, Math.min(i + 10, lines.size()));
                Thread t = new Thread(() -> splitLines(stopWords, splitResults, currentLines));
                t.start();
                wordThreads.add(t);
            }
            for (Thread wordThread : wordThreads) {
                wordThread.join();
            }

            // regroup

            Map<String, List<Pair<String, Integer>>> pairGroups = new HashMap<>();

//            splitResults.stream().flatMap(Collection::stream).forEach(
//                    pair -> {
//                        pairGroups.computeIfAbsent(pair.left, (_s) -> new ArrayList<>());
//                        pairGroups.get(pair.left).add(pair);
//                    }
//            );


            /**
             * here we have a list of pairs
             * now I add them in a map
             * word as key and all its repetitions as word -> [{word, 1},{word, 1},{word, 1},{word, 1}...]
             * now put the size of list as value into the map
             */
            for (List<Pair<String, Integer>> splitResult : splitResults) {
                for (Pair<String, Integer> pair : splitResult) {
                    pairGroups.computeIfAbsent(pair.left, (_s) -> new ArrayList<>());
                    pairGroups.get(pair.left).add(pair);
                }
            }

            // end regroup

            frequencies = pairGroups
                    .entrySet()
                    .stream()
                    .map(entry -> new Pair<>(entry.getKey(), entry.getValue().size()))
                    .collect(Collectors.toMap(it -> it.left, it -> it.right));


            printTop25();


        } catch (FileNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param stopWords
     * @param splitResults this is my concurrent list where my workers write
     * @param currentLines the data chunk
     */

    private static void splitLines(Set<String> stopWords, ConcurrentLinkedDeque<List<Pair<String, Integer>>> splitResults, List<String> currentLines) {
        /**
         * from the current lines split them based on regex
         * now check for non stop words and put it in the map
         * it will be like pairs of word and its count as 1
         */
        List<Pair<String, Integer>> results = currentLines
                .stream()
                .flatMap(it -> Arrays.stream(it.split("[^a-zA-Z0-9]+")))
                .map(it -> new Pair<>(it.toLowerCase(), 1))
                .filter(pair -> !stopWords.contains(pair.left) && pair.left.length() > 1)
                .collect(Collectors.toList());
        splitResults.add(results);
    }

    private static void printTop25() {
        List<Map.Entry<String, Integer>> top25 = frequencies.entrySet()
                .stream()
                //.filter(it -> it.getValue() != null)
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(25)
                .collect(Collectors.toList());
        System.out.println("-----------------------------");
        top25.forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
    }
}
