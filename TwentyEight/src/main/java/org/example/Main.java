package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * the actual output is the one after all the iterations
 * last 25 words is the output
 */

public class Main {

    /**
     * my first iterator that loads the file
     * this is a common iterator for both files
     */
    static class FileReader implements Iterator<String> {
        Scanner sc;

        /**
         * @param file      Name
         * @param delimiter that is used to get the words
         */
        public FileReader(File stopWords, String delimiter) {
            try {
                this.sc = new Scanner(stopWords).useDelimiter(delimiter);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * this is implementations of iterators
         * we need to fetch the next words from the file
         * and return true or false
         *
         * @return another one returns a word
         * @return true or false
         */
        @Override
        public boolean hasNext() {
            return sc.hasNext();
        }

        @Override
        public String next() {
            return sc.next().toLowerCase();
        }
    }

    /**
     * this iterator loads the non-stop words
     */
    static class NonStopWords implements Iterator<String> {

        List<String> stopWords = new ArrayList<>();
        Iterator<String> words;

        String current;

        public NonStopWords(File mainText, File stopWords) {
            /**
             * loads pride and prejudice
             */
            words = new FileReader(mainText, "[^a-zA-Z0-9]+");

            /**
             * loading stop words
             */
            Iterator<String> stop = new FileReader(stopWords, ",");
            while (stop.hasNext()) {
                this.stopWords.add(stop.next());
            }
        }

        /**
         * checks if a word is stop word or not
         *
         * @param word
         * @return true or false
         */
        boolean isNonStopWord(String word) {
            return !stopWords.contains(word) && word.length() >= 2;
        }

        /**
         * this is the iterator method that gives us the next word status
         * this just says if we have any more words left or not
         *
         * @return boolean
         */
        @Override
        public boolean hasNext() {
            /**
             * current is the current word if we have a word then check
             * if it is not a stopword
             * check if the current is not null if null load a new word from list
             * check if the list is empty before loading new word
             */
            if (current != null && isNonStopWord(current))
                return true;
            while (true) {
                if (!words.hasNext())
                    return false;
                current = words.next();
                if (isNonStopWord(current)) {
                    return true;
                }
            }
        }

        /**
         * this is the iterator method that gives us the next word
         * this checks if we have any more words left or not and returns them
         *
         * @return word/string
         */

        @Override
        public String next() {
            /**
             * current is the current word if we have a word then check
             * if it is not a stopword put it in a temp
             * now make current as null to indicate we have returned a word
             * check if the current is not null, if null load a new word from list
             * check if the list is empty before loading new word
             * throw an exception if the list is empty
             */
            while (true) {
                if (current != null && isNonStopWord(current)) {
                    String temp = current;
                    current = null;
                    return temp;
                } else {
                    if (!words.hasNext())
                        throw new RuntimeException();
                    current = words.next();
                }
            }
        }
    }

    /**
     * this iterator does my counting and adding words into filters
     */
    static class Counter implements Iterator<Map<String, Integer>> {

        Iterator<String> words;

        public Counter() {
            File mainText = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\pride-and-prejudice.txt");
            File stopWords = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\stop_words.txt");

            /**
             * loading my words
             * here I initialize my files
             */
            words = new NonStopWords(mainText, stopWords);
        }

        /**
         * counter variable to feed my printer or main function
         * chunk size is set to 5000
         */
        int i = 0;
        int CHUNK_SIZE = 5000;

        /**
         * to check if the words are not there
         */
        boolean isExhausted = false;
        Map<String, Integer> frequency = new HashMap<>();

        /**
         * checks if the list is exhausted or there is no more words to load
         *
         * @return boolean
         */
        @Override
        public boolean hasNext() {
            return words.hasNext() || !isExhausted;
        }

        /**
         * this checks if we don't have any more words
         * than it means we are at the end of the iteration
         * so flush the current map
         *
         * @return frequency map
         */
        @Override
        public Map<String, Integer> next() {
            while (true) {
                if (!words.hasNext()) {
                    // It is an error to try to get the final counts multiple times
                    if (isExhausted) {
                        throw new RuntimeException();
                    }
                    /**
                     * this is if the iteration is not a multiple of 5000
                     * but we still need to return
                     * this is for handling the last iteration
                     */
                    isExhausted = true;
                    return frequency;
                }
                /**
                 * load a word if the list is not empty
                 */

                String word = words.next();
                i++; // our counter should match amount of words retrieved
                /**
                 * regular put function
                 */
                if (frequency.containsKey(word)) {
                    frequency.put(word, frequency.get(word) + 1);
                } else {
                    frequency.putIfAbsent(word, 1);
                }

                /**
                 * return every 5000 iteration
                 */
                if (i % CHUNK_SIZE == 0) {
                    return frequency;
                }
            }
        }
    }


    public static void main(String[] args) {
        Counter counter = new Counter();
        /**
         * get the words sort and print it
         */
        while (counter.hasNext()) {
            Map<String, Integer> frequencies = counter.next();
            List<Map.Entry<String, Integer>> top25 = frequencies.entrySet()
                    .stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(25)
                    .collect(Collectors.toList());

            System.out.println("-----------------------------");
            top25.forEach(x -> System.out.println(x.getKey() + " - " + x.getValue()));
//            System.out.println(counter.i);

        }
    }


}
