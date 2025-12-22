package main;

import dictionary.DictionaryLoader;
import exception.FileReadException;
import exception.InvalidFileFormatException;
import translation.Translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String dictFile = "dictionary.csv";
        String inputFile = "input.txt";
        
        try {
            DictionaryLoader loader = new DictionaryLoader();
            Map<String, String> dictionary = loader.getDictionary(dictFile);
            Translator translator = new Translator(dictionary);

            try (InputStream inputStream = loader.openResource(inputFile);
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(translator.translate(line));
                }
            }

        } catch (FileReadException e) {
            System.out.println("Ошибка доступа к файлу: " + e.getMessage());
        } catch (InvalidFileFormatException e) {
            System.out.println("Ошибка формата словаря: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}

