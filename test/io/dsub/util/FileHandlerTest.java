package io.dsub.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    @Test
    void makeFile() throws IOException {
        File file = Files.createTempFile("_", "").toFile();
        file.delete();

        FileHandler.makeFile(file.toPath());
        assertNotNull(file);
        assertTrue(file.exists());

        file.delete();
    }

    @Test
    void readFile() throws IOException {
        File file = Files.createTempFile("_", "").toFile();
        File f2 = FileHandler.readFile(file.toURI());
        assertEquals(file, f2);
        file.delete();

        assertThrows(NoSuchFileException.class, () -> {
            FileHandler.readFile(file.toURI());
        });
    }
}