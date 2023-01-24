package Ex2;

import java.io.*;
import java.util.*;

public class Monolith {
    private static final List<String> stop_words = new ArrayList<>();
    private static List<String> words = new ArrayList<>();
    private static Map<String, Integer> frequencies = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        // loading stop words.
        // here we can use split function
        File file = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\stop_words.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] words = line.split(",");
            stop_words.addAll(Arrays.asList(words));
        }
        scanner.close();

        // reading words from pride-and-prejudice
        // read character by character
        // https://docs.oracle.com/javase/6/docs/api/java/io/Reader.html#read()
        File fileForWords = new File("C:\\WinterQuarter-23\\MSWE-262P\\src\\main\\resources\\pride-and-prejudice.txt");

        BufferedReader fr = new BufferedReader(new FileReader(fileForWords));

        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            int r;
            try {
                r = fr.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (r == -1) break;

            char c = (char) (r);
            if (Character.isLetterOrDigit(c)) {
                stringBuilder.append(c);
            } else {
                String word = new String(stringBuilder);
                if (!word.isEmpty())
                    words.add(word);
                stringBuilder = new StringBuilder();
            }
        }

        try {
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // filtering stop words from words
        // adding them into a frequency map
        for (String word : words) {
            String w = word.toLowerCase();
            if (!stop_words.contains(w) && w.length() >= 2) {
                if (frequencies.containsKey(w)) {
                    frequencies.put(w, frequencies.get(w) + 1);
                } else frequencies.put(w, 1);
            }
        }

        List<Map.Entry<String, Integer>> frequenciesEntries = new ArrayList<>();
        // counter for top 25
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            frequenciesEntries.add(entry);
        }

        // sorting the list in descending order
        for (int i = 0; i < frequenciesEntries.size(); i++) {
            for (int j = i + 1; j < frequenciesEntries.size(); j++) {
                int l = frequenciesEntries.get(i).getValue();
                int r = frequenciesEntries.get(j).getValue();
                if (r > l) {
                    Map.Entry<String, Integer> temp = frequenciesEntries.get(i);
                    frequenciesEntries.set(i, frequenciesEntries.get(j));
                    frequenciesEntries.set(j, temp);
                }
            }
        }

        // print top 25
        int iterator = 0;
        for (Map.Entry<String, Integer> printVal : frequenciesEntries) {
            if (iterator < 25) {
                iterator++;
                System.out.println(printVal.getKey() + " - " + printVal.getValue());
            }
        }
    }
}
