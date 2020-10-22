package io.dsub.util;

import io.dsub.constants.DataType;
import io.dsub.constants.StringConstants;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class FileHelper {
    public static Path getPath(DataType type) {
        return Path.of(System.getProperty("user.dir")
                .concat(File.separator)
                .concat(type.getFileName()));
    }

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
            throw new NoSuchFileException("failed to read file from path \"" + path.toString() + "\"");
        }

        return path.toFile();
    }

    public static void prune(File dir) throws IOException {
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File subFile : files) {
                prune(subFile);
            }
        }
        Files.delete(dir.toPath());
    }

    public static void pruneOnExit(File dir) throws IOException {
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File subFile : files) {
                prune(subFile);
            }
        }
        dir.deleteOnExit();
    }
}
