package io.dsub.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitializerTest {
    private static Path testPath;

    @Test
    void init() throws IOException {
        testPath = Files.createTempDirectory(getClass().getName());
        FileUtil.pruneOnExit(testPath.toFile());
        assertDoesNotThrow(() -> Initializer.init("test_schema.sql", "jdbc:h2:" + testPath.toAbsolutePath() + File.separator + "h2;MODE=MySQL"));
        File file = testPath.toFile();
        assertTrue(file.exists());
    }
}