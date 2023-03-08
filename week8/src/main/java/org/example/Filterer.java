package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

class Message<T> {
    T data;
    boolean end;

    /**
     *
     * @param data words
     * @param end this is my message to end thread
     */
    public Message(T data, boolean end) {
        this.data = data;
        this.end = end;
    }

    public Message(T data) {
        this.data = data;
        this.end = false;
    }

    /**
     *
     * @return no words but put the message as false
     * @param <T> string
     */
    public static <T> Message<T> endOfStream() {
        return new Message<>(null, true);
    }
}

public class Filterer implements Actor<Message<String>> {
    ArrayBlockingQueue<Message<String>> outputQueue = new ArrayBlockingQueue<>(10);

    ArrayBlockingQueue<Message<String>> mainText;
    ArrayBlockingQueue<Message<String>> stopWordsQueue;

    List<String> stopWords = new ArrayList<>();

    /**
     *
     * @param mainText get pride and prejudice
     * @param stopWords get the stop words
     */
    public Filterer(ArrayBlockingQueue<Message<String>> mainText, ArrayBlockingQueue<Message<String>> stopWords) {
        this.mainText = mainText;
        this.stopWordsQueue = stopWords;
    }

    @Override
    public ArrayBlockingQueue<Message<String>> getOutputChannel() {
        return outputQueue;
    }

    int emittedWords = 0;

    @Override
    public void run() {
        try {
            while (true) {
                /**
                 * Message has data and message to drive the loading
                 */
                Message<String> message = stopWordsQueue.take();
                if (message.end) break;
                stopWords.add(message.data);
            }
            System.out.println("Done reading stop words, got " + stopWords.size() + " words");

            /**
             * load main text and filter them
             */
            while (true) {
                Message<String> message = mainText.take();
                if (message.end) break;
                if (!stopWords.contains(message.data) && message.data.length() > 1) {
                    outputQueue.put(new Message<>(message.data));
                    emittedWords++;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("%d words left in queue", outputQueue.size());
        try {
            /**
             * the worker thread has ended
             */
            outputQueue.put(Message.endOfStream());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("End filterer: " + System.currentTimeMillis());
        System.out.println("Ending filterer, sent " + emittedWords + " words");
    }
}
