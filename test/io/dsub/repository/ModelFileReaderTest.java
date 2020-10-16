package io.dsub.repository;

import io.dsub.datasource.ModelFileReader;
import io.dsub.datasource.ModelFileWriter;
import io.dsub.datasource.ModelReader;
import io.dsub.datasource.ModelWriter;
import io.dsub.model.Transaction;
import io.dsub.util.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ModelFileReaderTest {

    private static final Logger logger = Logger.getLogger(ModelFileReaderTest.class.getName());
    private static ModelReader<Transaction> reader;
    private static List<Transaction> randomTransactions;
    private static File testFile;

    private static File populateFile() throws IOException {
        File file = File.createTempFile("_test", "");
        FileWriter writer = new FileWriter(file);
        file.deleteOnExit();
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
        testFile = populateFile();
        reader = new ModelFileReader<>(DataType.TRANSACTION, testFile);
    }

    @Test
    void readAll() {
        try {
            List<Transaction> list = reader.readAll();
            assertEquals(100, list.size());
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    @Test
    void testEmptyConstructor() {
        reader = new ModelFileReader<>(DataType.TRANSACTION, testFile);
        assertNotNull(((ModelFileReader<Transaction>) reader).getSourcePath());
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
            ModelWriter<Transaction> writer = new ModelFileWriter<>(DataType.TRANSACTION, testFile);
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