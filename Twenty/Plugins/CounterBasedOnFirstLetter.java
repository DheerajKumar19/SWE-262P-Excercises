

import MainFramework.Counter;

import java.util.*;

public class CounterBasedOnFirstLetter implements Counter {
    @Override
    public List<Map.Entry<String, Integer>> count(List<String> words) {
        String[] letter = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        Map<String, Integer> count = new HashMap<>();
        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        for (String l : letter) {
            count.put(l, 0);
        }
        for (String s : words) {
            if (count.containsKey(s.charAt(0) + "") && Character.isLetter(s.charAt(0))) {
                count.put((s.charAt(0) + ""), count.get(s.charAt(0) + "") + 1);
            } else {
                if (Character.isLetter(s.charAt(0)))
                    count.put((s.charAt(0) + ""), 1);
            }
        }
        ((List<Map.Entry<String, Integer>>) new ArrayList<>(count.entrySet())).sort(Comparator.comparing(Map.Entry::getKey));
        return new ArrayList<>(((List<Map.Entry<String, Integer>>) new ArrayList<>(count.entrySet())).subList(0, 26));
    }
}
