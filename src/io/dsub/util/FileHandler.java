package io.dsub.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.logging.Logger;

public class FileHandler {

    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());
    public static void makeFile(Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            System.out.println("IOException!\n" + e.getMessage());
        }
    }

    public static File readFile(URI uri) throws NoSuchFileException {
        Path path = Path.of(uri);

        if (Files.notExists(path)) {
            logger.severe("file not found");
            throw new NoSuchFileException("failed to read file from path \"" + path.toString() + "\"");
        }

        return path.toFile();
    }
}
