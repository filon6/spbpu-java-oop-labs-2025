package translation;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Translator {
    private final Map<String, String> dictionary;
    private final List<String> keysByLen;

    public Translator(Map<String, String> dictionary) {
        this.dictionary = dictionary;
        this.keysByLen = dictionary.keySet().stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .toList();
    }

    private boolean isBoundary(String s, int start, int end) {
        if (start != 0 && Character.isLetter(s.charAt(start - 1))) {
            return false;
        }
        return end == s.length() || !Character.isLetter(s.charAt(end));
    }


    public String translate(String line) {
        if (line == null || line.isEmpty()) {
            return line;
        }

        String lower = line.toLowerCase();
        StringBuilder stringBuilder = new StringBuilder(line.length());

        int index = 0;
        while (index < line.length()) {
            String bestKey = null;

            for (String key : keysByLen) {
                int end = index + key.length();
                if (end > line.length()) {
                    continue;
                }

                if (!lower.startsWith(key, index)) {
                    continue;
                }

                if (!isBoundary(lower, index, end)) {
                    continue;
                }

                bestKey = key;
                break;
            }

            if (bestKey != null) {
                stringBuilder.append(dictionary.get(bestKey));
                index += bestKey.length();
            } else {
                stringBuilder.append(line.charAt(index));
                index++;
            }
        }

        return stringBuilder.toString();
    }
}
