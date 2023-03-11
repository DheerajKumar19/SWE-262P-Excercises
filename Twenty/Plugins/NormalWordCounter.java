
import MainFramework.Counter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NormalWordCounter implements Counter {
    @Override
    public List<Map.Entry<String, Integer>> count(List<String> words) {
        Map<String, Integer> frequencies = new HashMap<>();

        for (String word : words) {
            String w = word.toLowerCase();
            if (frequencies.containsKey(w)) {
                frequencies.put(w, frequencies.get(w) + 1);
            } else frequencies.put(w, 1);
        }

        List<Map.Entry<String, Integer>> top25 = frequencies.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(25)
                .collect(Collectors.toList());

        return top25;

    }
}
