package thirty;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {
    Set<String> stopWords;
    BlockingQueue<String> words;
    BlockingQueue<Map<String, Integer>> partialFrequencies;

    Map<String, Integer> frequencies = new HashMap<>();

    /**
     * @param stopWords stopWords
     * @param words     main text
     * @param partialFrequencies queue to write in the processed words
     */
    public Worker(Set<String> stopWords, BlockingQueue<String> words, BlockingQueue<Map<String, Integer>> partialFrequencies) {
        this.stopWords = stopWords;
        this.words = words;
        this.partialFrequencies = partialFrequencies;
    }

    @Override
    public void run() {
        while (!words.isEmpty()) {
            String word = words.poll();
            /**
             * this condition is to check if there are no more words
             * and thread tries to access it and if it is null
             * break the loop
             * and put what it holds currently into my main queue
             */
            if (word == null) break;
            if (!stopWords.contains(word) && word.length() > 1) {
                if (frequencies.containsKey(word)) {
                    frequencies.put(word, frequencies.get(word) + 1);
                } else {
                    frequencies.put(word, 1);
                }
            }

        }
        try {
            partialFrequencies.put(frequencies);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
