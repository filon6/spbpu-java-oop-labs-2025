package dictionary;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import exception.FileReadException;
import exception.InvalidFileFormatException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DictionaryLoader {
    public InputStream openResource(String filename) throws FileReadException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);

        if (inputStream == null) {
            throw new FileReadException(filename + " не найден в resources");
        }

        return inputStream;
    }

    public Map<String, String> getDictionary(String dictionaryFilename) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                .addColumn("key")
                .addColumn("value")
                .setColumnSeparator('|')
                .build();

        try (InputStream inputStream = openResource(dictionaryFilename)) {

            MappingIterator<Map<String, String>> it = mapper
                    .readerFor(Map.class)
                    .with(schema)
                    .readValues(inputStream);

            Map<String, String> dictionary = new HashMap<>();
            while (it.hasNext()) {
                Map<String, String> row = it.next();
                String key = row.get("key");
                String value = row.get("value");

                if (key == null || value == null || key.trim().isEmpty() || value.trim().isEmpty()) {
                    throw new InvalidFileFormatException("Неправильная строка словаря: " + row);
                }

                dictionary.put(key.trim().toLowerCase(), value.trim());
            }
            return dictionary;
        }
    }
}
