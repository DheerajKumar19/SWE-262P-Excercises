package org.example;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * References => <a href="https://stackoverflow.com/questions/5343689/java-reading-a-file-into-an-arraylist">...</a>
 */

public class Stream {
    public static void main(String[] args) throws IOException {
        /**
         * using two args to load 2 files
         * Uri loader load my class paths
         */
        try {
            URI uri1 = ClassLoader.getSystemResource(args[0]).toURI();
            URI uri2 = ClassLoader.getSystemResource(args[1]).toURI();

            /**
             * this loads the stop words from file into a list
             */
            List<String> stop_words = Files.lines(Paths.get(uri1))
                    .flatMap(line -> Arrays.stream(line.split(","))).collect(Collectors.toList());

            /**
             * this will first load the pride and prejudice file
             * get it as a stream and splits based on the regex
             * puts into a map filtered based on stopwords and length
             * then we do a sort on this (descending)
             * set limit or get first 25
             * print them
             */
            Files.lines(Paths.get(uri2))
                    .flatMap(line -> Arrays.stream(line.split("[^a-zA-Z0-9]+")))
                    .map(String::toLowerCase)
                    .filter(x -> !stop_words.contains(x) && x.length() > 1)
                    .collect(Collectors.toMap(word -> word, word -> 1, (Integer::sum)))
                    .entrySet()
                    .stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(25)
                    .forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
        } catch (Exception e) {
            e.getMessage();
        }
    }
}