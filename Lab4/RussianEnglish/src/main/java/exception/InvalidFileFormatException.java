package exception;

import java.io.IOException;
// проблемы с файлом внутри
public class InvalidFileFormatException extends IOException {
    public InvalidFileFormatException(String message) {
        super(message);
    }
}
