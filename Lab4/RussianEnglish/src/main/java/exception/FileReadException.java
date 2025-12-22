package exception;

import java.io.IOException;
// файла не существует, нет доступа к файлу и т.д.
public class FileReadException extends IOException {
    public FileReadException(String message) {
        super(message);
    }
}
