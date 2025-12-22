package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static Methods.StreamMethods.*;

public class Main {
    public static void main(String[] args) {

        List<Integer> nums = List.of(12, 15);
        System.out.println("1. Среднее значение списка целых чисел");
        System.out.println("Вход: " + nums);
        System.out.println("Результат: " + average(nums));
        System.out.println();

        List<String> strings = List.of("Hello", "world");
        System.out.println("2. Строки -> UPPERCASE + префикс \"_new_\"");
        System.out.println("Вход: " + strings);
        System.out.println("Результат: " + addNewPrefixAndUppercase(strings));
        System.out.println();

        List<Integer> ints = List.of(1, 2, 2, 3);
        System.out.println("3. Квадраты элементов, встречающихся ровно 1 раз");
        System.out.println("Вход: " + ints);
        System.out.println("Результат: " + uniqueSquares(ints));
        System.out.println();

        List<String> strings2 = List.of("Hello", "world", "home", "House", "coffee");
        char prefix = 'H';
        System.out.println("4. Строки, начинающиеся с заданной буквы, отсортированные по алфавиту");
        System.out.println("Вход: " + strings2);
        System.out.println("Буква: '" + prefix + "'");
        System.out.println("Результат: " + filterAndSortByPrefix(strings2, prefix));
        System.out.println();

        Collection<Integer> c = List.of(10, 20, 30);
        System.out.println("5.Последний элемент коллекции или исключение, если коллекция пуста");
        System.out.println("Вход (Integer): " + c);
        System.out.println("Последний элемент: " + getLastElement(c));

        System.out.println("Вход (String): " + strings2);
        System.out.println("Последний элемент: " + getLastElement(strings2));

        Collection<Integer> collectionEmpty = new ArrayList<>();
        System.out.println("Вход (пустая коллекция): " + collectionEmpty);
        try {
            System.out.println("Последний элемент: " + getLastElement(collectionEmpty));
        } catch (IllegalArgumentException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
        System.out.println();

        int[] arr = {1,2,3,4,5,6,7,8,9,10};
        System.out.println("6. Сумма чётных чисел массива или 0, если чётных нет");
        System.out.print("Вход: ");
        printArray(arr);
        System.out.println("Результат: " + sumOfEvenNumbers(arr));

        int[] arrNoEven = {1,3,5,7,9};
        System.out.print("Вход (без чётных): ");
        printArray(arrNoEven);
        System.out.println("Результат: " + sumOfEvenNumbers(arrNoEven));
        System.out.println();

        System.out.println("7. Преобразование списка строк в Map: первый символ -> ключ, остальное -> значение");
        List<String> input = List.of("Hello", "World", "Java");
        System.out.println("Вход: " + input);
        Map<Character, String> map1 = stringsToCharMap(input);
        System.out.println("Результат: " + map1);

        List<String> input2 = List.of("Hello", "World", "Java", "Home", "House");
        System.out.println("Вход (ожидается дубликат ключа 'H'): " + input2);
        try {
            System.out.println("Результат: " + stringsToCharMap(input2));
        } catch (IllegalStateException e) {
            System.out.println("Исключение: " + e.getMessage());
        }
    }

    private static void printArray(int[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}
