package io.dsub.repository;

import io.dsub.AppState;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;
import io.dsub.util.Initializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRepositoryTest {
    private static Connection conn = null;
    private static String initSql = null;
    private static Path testPath = null;
    private static TransactionRepository transactionRepository;
    private static VendorRepository vendorRepository;

    @BeforeEach
    void init() throws SQLException, IOException {
        testPath = Files.createTempDirectory(getClass().getName());
        assertDoesNotThrow(() -> Initializer.init("test_schema.sql", "jdbc:h2:" + testPath.toAbsolutePath() + File.separator + "h2;MODE=MySQL"));
        conn = AppState.getInstance().getConn();
        InputStream sqlStream = CategoryRepositoryTest.class.getClassLoader().getResourceAsStream("test_schema.sql");

        assertNotNull(sqlStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(sqlStream));

        initSql = reader.lines()
                .reduce((acc, curr) -> acc + curr + "\n")
                .orElse("");

        transactionRepository = new TransactionRepository(conn);
        vendorRepository = new VendorRepository(conn);
        conn.createStatement().execute(initSql);
    }

    @Test
    void findAll() {
        try {
            vendorRepository.save(new Vendor("first"));
            Transaction t1 = new Transaction(33, 1);
            Transaction t2 = new Transaction(32891273, 1);

            transactionRepository.save(t1);
            transactionRepository.save(t2);

            assertEquals(2, transactionRepository.findAll().size());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findById() {
        try {
            vendorRepository.save(new Vendor("first"));
            Transaction t1 = new Transaction(33, 1);
            Transaction t2 = new Transaction(32891273, 1);

            String t1Id = transactionRepository.save(t1);
            String t2Id = transactionRepository.save(t2);

            Transaction foundFirst = transactionRepository.findById(t1Id);
            Transaction foundSecond = transactionRepository.findById(t2Id);

            assertEquals(t1, foundFirst);
            assertEquals(t2, foundSecond);

        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    // TODO: TEST!!!!!
    @Test
    void deleteById() {
    }

    @Test
    void count() {
        try {
            vendorRepository.save(new Vendor("first"));
            Transaction t1 = new Transaction(33, 1);
            transactionRepository.save(t1);
            assertEquals(1, transactionRepository.count());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findByName() {
        assertThrows(UnsupportedOperationException.class, () -> transactionRepository.findByName("throw"));
    }

    @Test
    void deleteByName() {
        assertThrows(UnsupportedOperationException.class, () -> transactionRepository.deleteByName("throw"));
    }

    @Test
    void save() {
        try {
            Transaction first = new Transaction(33, 3);
            assertThrows(SQLException.class, () -> transactionRepository.save(first));

            vendorRepository.save(new Vendor("first"));
            vendorRepository.save(new Vendor("second"));
            assertEquals(2, vendorRepository.count());

            Transaction second = new Transaction(33, 2);
            assertDoesNotThrow(() -> transactionRepository.save(second));
            Transaction third = new Transaction(33, 1);
            assertDoesNotThrow(() -> transactionRepository.save(third));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void saveAll() {
        try {
            vendorRepository.save(new Vendor("first"));
            vendorRepository.save(new Vendor("second"));
            vendorRepository.save(new Vendor("third"));

            Transaction t1 = new Transaction(331223, 1);
            Transaction t2 = new Transaction(143324, 2);
            Transaction t3 = new Transaction(32415, 1);
            Transaction t4 = new Transaction(31233, 3);
            Transaction t5 = new Transaction(112, 1);

            List<Transaction> transactions = new ArrayList<>(Arrays.asList(t1, t2, t3, t4, t5));
            transactionRepository.saveAll(transactions);

            assertEquals(5, transactionRepository.count());

            Transaction toFail = new Transaction(12312312, 78);
            List<Transaction> other = new ArrayList<>(Collections.singletonList(toFail));
            assertThrows(SQLException.class, () -> transactionRepository.saveAll(other));
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void delete() {
        try {
            vendorRepository.save(new Vendor("first"));
            Transaction t1 = new Transaction(33, 1);
            transactionRepository.save(t1);
            assertEquals(1, transactionRepository.count());
            assertDoesNotThrow(() -> transactionRepository.delete(t1));
            assertEquals(0, transactionRepository.count());
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testRangedQuery() {
        try {
            vendorRepository.save(new Vendor("first"));
            List<Transaction> transactions = makeTransactions(10000, 1);

            transactionRepository.saveAll(transactions);
            Random random = new Random();
            LocalDate start = LocalDate.now().minusDays(random.nextInt(365));
            LocalDate end = LocalDate.now();

            List<Transaction> result = transactionRepository.findBetween(start, end);
            for (Transaction item : result) {
                LocalDate target = item.getDate();
                assertTrue(target.isAfter(start) || target.isEqual(start));
                assertTrue(target.isBefore(end) || target.isEqual(end));
            }
        } catch (SQLException e) {
            fail(e.getMessage());
        }
    }

    private List<Transaction> makeTransactions(int count, int vendorId) {
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            LocalDate date = LocalDate.now().minusDays(random.nextInt(450));
            Transaction transaction = new Transaction(random.nextInt(30325), vendorId, date);
            transactions.add(transaction);
        }

        return transactions;
    }

}