package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File mainText = new File("C:\\WinterQuarter-23\\262P-Project\\week8\\src\\main\\java\\org\\example\\pride-and-prejudice.txt");
        File stopWords = new File("C:\\WinterQuarter-23\\262P-Project\\week8\\src\\main\\java\\org\\example\\stop_words.txt");

        try {
            Scanner pnp = new Scanner(mainText).useDelimiter("[^a-zA-Z0-9]+");
            Scanner stop = new Scanner(stopWords).useDelimiter(",\\s?");

            /**
             * starting my actors to load my main text and stop words
             */
            WordLoader loadPnp = new WordLoader(pnp);
            WordLoader loadStop = new WordLoader(stop);

            /**
             * filter is my actor that writes his work to a output channel
             */
            Filterer filterer = new Filterer(loadPnp.getOutputChannel(), loadStop.getOutputChannel());

            /**
             * here actual counting and map reduce and print happens
             */
            Counter counter = new Counter(filterer.getOutputChannel());

            Thread[] threads = {new Thread(filterer), new Thread(counter), new Thread(loadPnp), new Thread(loadStop)};
            for (Thread t : threads) {
                t.start();
            }
            for (Thread t : threads) {
                t.join();
            }
        } catch (FileNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}