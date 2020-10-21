package io.dsub.repository;

import io.dsub.util.Initializer;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

class CategoryRepositoryTest {
    @BeforeAll
    static void init() throws SQLException {
        Initializer.init("test_schema", "jdbc:h2:" + System.getProperty("user.dir") + File.separator + "test" + File.separator + "h2;MODE=MySQL");

    }
    @AfterAll
    static void cleanUp() throws IOException {
        Path path = Path.of(System.getProperty("user.dir") + File.separator + "test");
        Files.deleteIfExists(path);
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void find() {
    }

    @Test
    void findByName() {
    }

    @Test
    void findAll() {
    }

    @Test
    void save() {
    }

    @Test
    void saveAll() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteByName() {
    }
}