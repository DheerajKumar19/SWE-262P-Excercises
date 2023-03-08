package thirtytwo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {
    Set<String> stopWords;
    List<String> words;
    BlockingQueue<Map<String, Integer>> partialFrequencies;

    Map<String, Integer> frequencies = new HashMap<>();

    public Worker(Set<String> stopWords, List<String> words, BlockingQueue<Map<String, Integer>> partialFrequencies) {
        this.stopWords = stopWords;
        this.words = words;
        this.partialFrequencies = partialFrequencies;
    }

    @Override
    public void run() {
        for(String word: words) {
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
