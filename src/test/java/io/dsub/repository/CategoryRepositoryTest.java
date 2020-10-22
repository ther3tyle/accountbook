package io.dsub.repository;

import io.dsub.AppState;
import io.dsub.Application;
import io.dsub.constants.DataType;
import io.dsub.constants.UIString;
import io.dsub.model.Category;
import io.dsub.util.Initializer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {
    private static Connection conn = null;
    private static String initSql = null;
    private static Path testPath = null;
    private static CategoryRepository repository;

    @BeforeEach
    void init() throws SQLException, IOException {
        testPath = Files.createTempDirectory("");
        assertDoesNotThrow(() -> Initializer.init("test_schema.sql", "jdbc:h2:" + testPath.toAbsolutePath() + File.separator + "h2;MODE=MySQL"));
        conn = AppState.getInstance().getConn();
        InputStream sqlStream = CategoryRepositoryTest.class.getClassLoader().getResourceAsStream("test_schema.sql");

        assertNotNull(sqlStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(sqlStream));

        initSql = reader.lines()
                .reduce((acc, curr) -> acc + curr + "\n")
                .orElse("");

        repository = new CategoryRepository(conn);
        conn.createStatement().execute(initSql);
    }


    @Test
    void find() throws SQLException {
        assertDoesNotThrow(this::insertThreeDummies);
        Category item;

        for (int i = 1; i < 4; i++) {
            item = repository.findById(String.valueOf(i));
            assertNotNull(item);
        }

        item = repository.findById(String.valueOf(4));
        assertNull(item);
    }

    @Test
    void findByName() throws SQLException {
        assertDoesNotThrow(this::insertThreeDummies);
        Category item = repository.findByName("hello");
        assertNull(item);
        item = repository.findByName("Hello");
        assertNotNull(item);
    }

    @Test
    void findAll() {
        assertDoesNotThrow(this::insertThreeDummies);
        try {
            List<Category> items = repository.findAll();
            assertNotNull(items);
            assertEquals(3, items.size());
        } catch (SQLException e) {
            fail("unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void save() {
        assertDoesNotThrow(() -> repository.save(new Category("New World Order")));
        assertDoesNotThrow(() -> {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM " + Application.SCHEMA_NAME + "." + DataType.CATEGORY.getTableName() + " WHERE id = 1");
            rs.next();
            assertEquals("New World Order", rs.getString("name"));
            assertEquals(1, rs.getInt("id"));
        });

        // duplicated then create new item
        assertDoesNotThrow(() -> repository.save(new Category("New World Order")));

        try {
            Category toUpsert = new Category(1, "NEW NAME");
            repository.save(toUpsert);
            assertEquals("NEW NAME", repository.findById("1").getName());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void saveAll() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("hello"));
        categories.add(new Category("mister"));
        categories.add(new Category("william"));

        assertDoesNotThrow(() -> repository.saveAll(categories));
        assertDoesNotThrow((ThrowingSupplier<ArrayList<Category>>) ArrayList::new);

        assertDoesNotThrow(() -> {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM " + Application.SCHEMA_NAME + "." + DataType.CATEGORY.getTableName());
            int count = 0;
            while(rs.next()) {
                count++;
            }
            assertEquals(3, count);
        });
    }

    @Test
    void delete() {
        assertDoesNotThrow(this::insertThreeDummies);
        assertDoesNotThrow(() -> {
            Category item = repository.findById(String.valueOf(1));
            repository.delete(item);
            repository.delete(new Category("World"));
            assertThrows(InvalidParameterException.class, () -> repository.delete(new Category(3, null)));

            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM " + Application.SCHEMA_NAME + "." + DataType.CATEGORY.getTableName());

            int count = 0;
            while (rs.next()) {
                count++;
            }

            assertEquals(1, count);
        });


    }

    @Test
    void deleteById() {
        assertDoesNotThrow(this::insertThreeDummies);
        assertDoesNotThrow(() -> assertEquals(3, repository.count()));
        assertDoesNotThrow(() -> repository.deleteById("1"));
        assertDoesNotThrow(() -> repository.deleteById("2"));
        assertDoesNotThrow(() -> repository.deleteById("3"));
        assertDoesNotThrow(() -> repository.deleteById("4")); // should not throw even if the item is not present
        assertDoesNotThrow(() -> assertEquals(0, repository.count()));
    }

    @Test
    void deleteByName() {
        assertDoesNotThrow(this::insertThreeDummies);
        assertDoesNotThrow(() -> assertEquals(3, repository.count()));
        assertDoesNotThrow(() -> repository.deleteByName("Hello"));
        assertDoesNotThrow(() -> repository.deleteByName("World"));
        assertDoesNotThrow(() -> repository.deleteByName("JAMES"));
        assertDoesNotThrow(() -> assertEquals(1, repository.count()));
    }

    @Test
    void testCount() {
        assertDoesNotThrow(this::insertThreeDummies);
        assertDoesNotThrow(() -> {
            assertEquals(3, repository.count());
        });
    }

    void insertThreeDummies() throws SQLException {
        conn.createStatement().execute("INSERT INTO " + Application.SCHEMA_NAME + "." + DataType.CATEGORY.getTableName() + " (name) VALUES ('Hello')");
        conn.createStatement().execute("INSERT INTO " + Application.SCHEMA_NAME + "." + DataType.CATEGORY.getTableName() + " (name) VALUES ('World')");
        conn.createStatement().execute("INSERT INTO " + Application.SCHEMA_NAME + "." + DataType.CATEGORY.getTableName() + " (name) VALUES ('James')");
    }
}