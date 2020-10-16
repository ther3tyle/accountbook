package io.dsub.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class FileHelperTest {

    @Test
    void getPath() {
        assertThrows(NullPointerException.class, () -> FileHelper.getPath(null));
        Path path = FileHelper.getPath(DataType.TRANSACTION);
        assertNotNull(path);
        assertTrue(path.toString().contains(System.getProperty("user.dir")));
    }

    @Test
    void makeFile() {
        var ref = new Object() {
            File file;
        };

        assertDoesNotThrow(() -> ref.file = Files.createTempFile("_", "").toFile());

        File file = ref.file;

        file.delete();

        FileHelper.makeFile(file.toPath());
        assertNotNull(ref.file);
        assertTrue(file.exists());

        file.delete();
    }

    @Test
    void readFile() {
        var ref = new Object() {
            File file;
        };

        assertDoesNotThrow(() -> ref.file = Files.createTempFile("_", "").toFile());
        File file = ref.file;

        assertDoesNotThrow(() -> ref.file = FileHelper.readFile(file.toURI()));
        File f2 = ref.file;

        assertEquals(file, f2);
        assertTrue(file.delete());

        Logger logger = Logger.getLogger(FileHelper.class.getName());
        Level logLev = logger.getLevel();
        logger.setLevel(Level.OFF);
        assertThrows(NoSuchFileException.class, () -> FileHelper.readFile(file.toURI()));
        logger.setLevel(logLev);
    }
}