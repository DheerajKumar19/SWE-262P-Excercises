package thirty;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class Main {

    /**
     * defining my queues
     */
    static BlockingQueue<String> words = new LinkedBlockingQueue<>();
    static BlockingQueue<Map<String, Integer>> partialFrequencies = new LinkedBlockingQueue<>();
    static Map<String, Integer> frequencies = new HashMap<>();


    public static void main(String[] args) {
        File mainText = new File("C:\\WinterQuarter-23\\MSWE-262P\\week8\\src\\main\\java\\org\\example\\pride-and-prejudice.txt");
        File stopWordsFile = new File("C:\\WinterQuarter-23\\MSWE-262P\\week8\\src\\main\\java\\org\\example\\stop_words.txt");
        Set<String> stopWords = new HashSet<>();
        try {
            Scanner pnp = new Scanner(mainText).useDelimiter("[^a-zA-Z0-9]+");
            Scanner stop = new Scanner(stopWordsFile).useDelimiter(",\\s?");

            /**
             * load stop words
             */
            while (stop.hasNext()) {
                stopWords.add(stop.next());
            }

            /**
             * load main text
             */
            while (pnp.hasNext()) {
                words.put(pnp.next().toLowerCase());
            }

            List<Thread> workers = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                /**
                 * feed my worker threads the stop wrods, main text and a queue to put their work
                 */
                Worker worker = new Worker(stopWords, words, partialFrequencies);
                workers.add(new Thread(worker));
            }
            for (Thread worker : workers) {
                worker.start();
            }
            for (Thread worker : workers) {
                worker.join();
            }

            /**
             * here map reduce happens
             */
            frequencies = partialFrequencies
                    .stream()
                    .flatMap(it -> it.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Integer::sum));

//            // reduce iterative way
//            while (!partialFrequencies.isEmpty()) {
//                Map<String, Integer> current = partialFrequencies.poll();
//                for (Map.Entry<String, Integer> entry : current.entrySet()) {
//                    if (frequencies.containsKey(entry.getKey())) {
//                        frequencies.put(entry.getKey(), frequencies.get(entry.getKey()) + entry.getValue());
//                    } else {
//                        frequencies.put(entry.getKey(), entry.getValue());
//                    }
//                }
//
//            }

            printTop25();


        } catch (FileNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
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
