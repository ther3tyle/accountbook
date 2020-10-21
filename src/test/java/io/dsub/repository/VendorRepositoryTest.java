package io.dsub.repository;

import io.dsub.AppState;
import io.dsub.model.Category;
import io.dsub.model.Vendor;
import io.dsub.util.FileHelper;
import io.dsub.util.Initializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VendorRepositoryTest {

    private static Connection conn = null;
    private static String initSql = null;
    private static Path testPath = null;
    private static VendorRepository repository;


    @BeforeEach
    void init() throws SQLException, IOException {
        testPath = Files.createTempDirectory("test");
        assertDoesNotThrow(() -> Initializer.init("test_schema.sql", "jdbc:h2:" + testPath.toAbsolutePath() + File.separator + "h2;MODE=MySQL"));
        conn = AppState.getInstance().getConn();
        InputStream sqlStream = CategoryRepositoryTest.class.getClassLoader().getResourceAsStream("reset_schema.sql");

        assertNotNull(sqlStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(sqlStream));

        initSql = reader.lines()
                .reduce((acc, curr) -> acc + curr + "\n")
                .orElse("");

        repository = new VendorRepository(conn);
        conn.createStatement().execute(initSql);
    }

    @AfterEach
    void cleanUp() throws IOException {
        FileHelper.prune(testPath.toFile());
    }

    @Test
    void findAll() {
        try {
            Vendor vendor1 = new Vendor("hello");
            Vendor vendor2 = new Vendor("world");

            repository.save(vendor1);
            repository.save(vendor2);

            assertEquals(2, repository.findAll().size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findById() {
        try {
            Vendor vendor1 = new Vendor("hello");
            Vendor vendor2 = new Vendor("world");

            repository.save(vendor1);
            repository.save(vendor2);

            Vendor v1 = repository.findById("1");
            Vendor v2 = repository.findById("2");

            assertNotEquals(v1, v2);
            assertNotNull(v1);
            assertNotNull(v2);

            assertNull(repository.findById("3"));
            assertEquals(2, repository.findAll().size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findByName() {
        try {
            Vendor vendor1 = new Vendor("hello");
            Vendor vendor2 = new Vendor("world");

            repository.save(vendor1);
            repository.save(vendor2);

            Vendor vendor = repository.findByName("hello");
            assertEquals(vendor1.getName(), vendor.getName());
            assertEquals("1", vendor.getId());
            assertNull(vendor.getCatId());


        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteById() {
        try {
            Vendor vendor1 = new Vendor("hello");
            Vendor vendor2 = new Vendor("world");

            repository.save(vendor1);
            repository.save(vendor2);

            repository.deleteById("1");
            repository.deleteById("2");

            assertEquals(0, repository.count());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void count() {
        try {
            Vendor vendor1 = new Vendor("hello");
            Vendor vendor2 = new Vendor("world");

            repository.save(vendor1);
            repository.save(vendor2);

            assertEquals(2, repository.count());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteByName() {
        try {
            Vendor vendor1 = new Vendor("hello");
            Vendor vendor2 = new Vendor("world");

            repository.save(vendor1);
            repository.save(vendor2);

            repository.deleteByName("hello");
            repository.deleteByName("world");

            assertEquals(0, repository.count());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void save() {
        Vendor vendor = new Vendor("hello");
        assertDoesNotThrow(() -> repository.save(vendor));
        assertThrows(SQLException.class, () -> repository.save(new Vendor("hello")));
        assertThrows(SQLException.class, () -> repository.save(new Vendor("Other", 3)));

        try {
            CategoryRepository categoryRepository = new CategoryRepository(conn);
            categoryRepository.save(new Category("my category"));
            assertDoesNotThrow(() -> repository.save(new Vendor("Other", 1)));
            assertThrows(SQLException.class, () -> repository.save(new Vendor("So", 2)));
        } catch (SQLException e) {
            fail("unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void saveAll() {
        Vendor vendor1 = new Vendor("hello");
        Vendor vendor2 = new Vendor("world");
        List<Vendor> vendors = new ArrayList<>();
        vendors.add(vendor1);
        vendors.add(vendor2);

        assertDoesNotThrow(() -> repository.saveAll(vendors));
        assertDoesNotThrow(() -> assertEquals(2, repository.count()));
    }

    @Test
    void delete() {
        try {
            Vendor vendor = new Vendor("hello");
            Vendor other = new Vendor("world");

            assertDoesNotThrow(() -> repository.save(vendor));
            assertDoesNotThrow(() -> repository.save(other));
            assertEquals(2, repository.count());
            assertDoesNotThrow(() -> repository.delete(vendor));
            assertEquals(1, repository.count());
            assertDoesNotThrow(() -> repository.delete(other));
            assertEquals(0, repository.count());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testDeleteByName() {
        try {
            Vendor vendor = new Vendor("hello");
            Vendor other = new Vendor("world");

            assertDoesNotThrow(() -> repository.save(vendor));
            assertDoesNotThrow(() -> repository.save(other));
            assertEquals(2, repository.count());
            assertDoesNotThrow(() -> repository.deleteByName(vendor.getName()));
            assertEquals(1, repository.count());
            assertDoesNotThrow(() -> repository.deleteByName(other.getName()));
            assertEquals(0, repository.count());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }
}