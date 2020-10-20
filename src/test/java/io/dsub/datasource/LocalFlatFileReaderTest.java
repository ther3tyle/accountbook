package io.dsub.datasource;

import io.dsub.datasource.reader.LocalFlatFileReader;
import io.dsub.datasource.reader.ModelReader;
import io.dsub.datasource.writer.LocalFlatFileWriter;
import io.dsub.datasource.writer.ModelWriter;
import io.dsub.model.Transaction;
import io.dsub.constants.DataType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LocalFlatFileReaderTest {

    private static ModelReader<Transaction> reader;
    private static List<Transaction> randomTransactions;
    private static File testFile;

    private static File getPopulatedFile(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        for (Transaction t : randomTransactions) {
            writer.append(t.toString());
        }
        writer.flush();
        writer.close();
        return file;
    }

    private static List<Transaction> getRandomData() {
        List<Transaction> transactions = new ArrayList<>();
        int count = 0;
        Random rand = new Random();
        while (count++ < 100) {
            Transaction t = new Transaction(rand.nextInt(100), rand.nextInt(100));
            while (t.getId().equals("000-0000-0000-0000-000000000000")) {
                t = new Transaction(rand.nextInt(100), rand.nextInt(5));
            }
            transactions.add(t);
        }
        return transactions;
    }

    @BeforeEach
    void prepTest() throws IOException {
        randomTransactions = getRandomData();
        Path path = Files.createTempFile("_test", "");
        testFile = path.toFile();
        testFile.createNewFile();

        testFile = getPopulatedFile(testFile);
        reader = new LocalFlatFileReader<>(DataType.TRANSACTION, testFile);
    }

    @AfterEach
    void cleanUp() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void readAll() {
        assertDoesNotThrow(() -> {
            List<Transaction> list = reader.readAll();
            assertEquals(100, list.size());
        });
    }

    @Test
    void read() {
        assertThrows(NoSuchElementException.class, () -> {
                    String uuidString = randomTransactions.get(new Random().nextInt(randomTransactions.size() - 1)).getId();
                    Transaction t = reader.readByKey(uuidString);
                    assertNotNull(t);
                    assertNull(reader.readByKey("000-0000-0000-0000-000000000000"));
                }
        );

        assertDoesNotThrow(() -> {
            testFile.delete();
            testFile.createNewFile();
        });

        assertThrows(NoSuchElementException.class, () -> {
            Transaction t = reader.read();
        });
    }

    @Test
    void testPruneDuplicatedEntry() {
        assertDoesNotThrow(() -> {
            Transaction t = reader.read(); // reads first transaction
            ModelWriter<Transaction> writer = new LocalFlatFileWriter<>(DataType.TRANSACTION, testFile);
            writer.reset();

            writer.write(t);
            writer.write(t);
            assertDoesNotThrow(() -> assertNotNull(reader.readByKey(t.getId())));

            int count = (int) reader.readAll()
                    .stream()
                    .filter(transaction -> transaction.getId().equals(t.getId()))
                    .count();
            assertEquals(1, count);
        });
    }
}