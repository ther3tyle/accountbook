package io.dsub.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class InitializerTest {
    private static final String TEST_PATH = System.getProperty("user.dir") + File.separator + "test";

    @AfterEach
    void cleanUp() throws IOException {
        File testFile = Path.of(TEST_PATH).toFile();
        prune(testFile);
        assertFalse(testFile.exists());
    }

    @Test
    void init() {
        assertDoesNotThrow(() -> Initializer.init("test_schema.sql", "jdbc:h2:" + TEST_PATH + File.separator + "h2;MODE=MySQL"));
        assertDoesNotThrow(() -> Initializer.init("test_schema.sql", "jdbc:h2:" + TEST_PATH + File.separator + "h2;MODE=MySQL"));
        File file = Path.of(TEST_PATH).toFile();
        assertTrue(file.exists());
    }

    private void prune(File dir) throws IOException {
        if (!dir.exists()) return;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File subFile : files) {
                prune(subFile);
            }
        }
        Files.delete(dir.toPath());
    }
}