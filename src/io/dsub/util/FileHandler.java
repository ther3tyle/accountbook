package io.dsub.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {
    public static void makeFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            System.out.println("IOException!\n" + e.getLocalizedMessage());
        }
    }

    public static File readFile(URI uri) {
        Path path = Path.of(uri + "/" + DataType.TRANSACTION_FILE.getValue());

        if (Files.notExists(path)) {
            System.out.println("file not found!");
            System.exit(1);
        }

        return path.toFile();
    }
}
