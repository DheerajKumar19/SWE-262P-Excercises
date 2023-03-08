package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

public class Counter implements Actor<Map.Entry<String, Integer>> {
    ArrayBlockingQueue<Message<String>> filteredWords;
    Map<String, Integer> frequencies = new HashMap<>();

    public Counter(ArrayBlockingQueue<Message<String>> outputChannel) {
        filteredWords = outputChannel;
    }

    @Override
    public ArrayBlockingQueue<Map.Entry<String, Integer>> getOutputChannel() {
        return null;
    }

    int receivedWords = 0;

    void getWords() throws InterruptedException {
        while (true) {
            Message<String> message = filteredWords.take();
            receivedWords++;
            /**
             * if end is reached break the loop
             */
            if (message.end) {
                System.out.printf("%d words left in queue\n", filteredWords.size());
//                System.out.format("Processed %d words\n", receivedWords);
//                System.out.println("Got end from filterer: " + System.currentTimeMillis());
                return;
            }

            /**
             * here I put the words into a map
             */
            if (frequencies.containsKey(message.data))
                frequencies.put(message.data, frequencies.get(message.data) + 1);
            else
                frequencies.put(message.data, 1);
        }
    }

    @Override
    public void run() {
        try {
            getWords();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /**
         * printing is done once all the workers have written to map
         */
        printTop25();
    }

    private void printTop25() {
        List<Map.Entry<String, Integer>> top25 = frequencies.entrySet()
                .stream()
                //.filter(it -> it.getValue() != null)
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(25)
                .collect(Collectors.toList());
        System.out.println("-----------------------------");
        top25.forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
        System.out.println("Ending counter, recieved " + receivedWords + " words");
        System.out.format("Had %d words in queue", filteredWords.size());
    }
}
