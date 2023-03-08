package org.example;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class WordLoader implements Actor<Message<String>> {

    ArrayBlockingQueue<Message<String>> outputChannel = new ArrayBlockingQueue<>(10);

    Scanner sc;

    public WordLoader(Scanner sc) {
        this.sc = sc;
    }

    @Override
    public ArrayBlockingQueue<Message<String>> getOutputChannel() {
        return outputChannel;
    }

    /**
     * just read the file
     * put words into the queue/output stream
     */
    @Override
    public void run() {
        while (sc.hasNext()) {
            try {
                outputChannel.put(new Message<>(sc.next().toLowerCase()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            outputChannel.put(Message.endOfStream());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Ending word loader");
    }
}
