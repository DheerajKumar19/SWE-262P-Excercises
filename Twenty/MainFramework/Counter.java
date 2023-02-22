package MainFramework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Counter {
    List<Map.Entry<String, Integer>> count(List<String> words);
}
