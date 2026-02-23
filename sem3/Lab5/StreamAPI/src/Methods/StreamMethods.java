package Methods;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class StreamMethods {
    public static double average(List<Integer> list) {
        return list.stream()
                //.mapToInt(Integer::intValue)
                .mapToInt(new ToIntFunction<Integer>() {
                    @Override
                    public int applyAsInt(Integer value) {
                        return value;
                    }
                })
                .average()
                .orElse(0.0);
    }

    public static List<String> addNewPrefixAndUppercase(List<String> stringList) {
        return stringList.stream()
                .map(s -> "_new_" + s.toUpperCase())
                .toList();
    }

    public static List<Integer> uniqueSquares(List<Integer> list) {
        Map<Integer, Long> freq = list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return list.stream()
                .filter(x -> freq.get(x) == 1)
                .map(x -> x * x)
                .toList();
    }

    public static List<String> filterAndSortByPrefix(Collection<String> strings, char letter) {
        char lower = Character.toLowerCase(letter);

        return strings.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty() && Character.toLowerCase(s.charAt(0)) == lower)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }

    public static <T> T getLastElement(Collection<T> collection) {
        return collection.stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new IllegalArgumentException("Коллекция пуста"));
    }

    public static int sumOfEvenNumbers(int[] array) {
        return Arrays.stream(array)
                .filter(x -> x % 2 == 0)
                .sum();
    }

    public static Map<Character, String> stringsToCharMap(List<String> list) {
        return list.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toMap(
                        s -> s.charAt(0),
                        s -> s.substring(1)
                ));
    }
}