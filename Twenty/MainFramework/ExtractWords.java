package MainFramework;

import java.io.IOException;
import java.util.List;

public interface ExtractWords {
    List<String> extractWords(String filePath) throws IOException;
}
