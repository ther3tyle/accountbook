package io.dsub.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class InitializerTest {
    private static Path testPath;

    @AfterEach
    void cleanUp() throws IOException {
        File testFile = testPath.toFile();
        FileHelper.prune(testFile);
        assertFalse(testFile.exists());
    }

    @Test
    void init() throws IOException {
        testPath = Files.createTempDirectory("test");
        assertDoesNotThrow(() -> Initializer.init("test_schema.sql", "jdbc:h2:" + testPath.toAbsolutePath() + File.separator + "h2;MODE=MySQL"));
        File file = testPath.toFile();
        assertTrue(file.exists());
    }
}