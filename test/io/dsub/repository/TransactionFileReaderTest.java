package io.dsub.repository;

import io.dsub.datasource.ModelReader;
import io.dsub.datasource.ModelWriter;
import io.dsub.datasource.TransactionFileReader;
import io.dsub.datasource.TransactionFileWriter;
import io.dsub.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class TransactionFileReaderTest {

    private static final Logger logger = Logger.getLogger(TransactionFileReaderTest.class.getName());
    private static ModelReader<Transaction, UUID> reader;
    private static List<Transaction> randomTransactions;
    private static File testFile;

    @BeforeEach
    void prepTest() throws IOException {
        randomTransactions = getRandomData();
        testFile = populateFile();
        reader = new TransactionFileReader(testFile.toPath());
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
        reader = new TransactionFileReader();
        assertNotNull(((TransactionFileReader) reader).getSource());
    }

    @Test
    void read() {
        assertDoesNotThrow(() -> {
                    Transaction t = reader.readByKey(randomTransactions.get(new Random().nextInt(randomTransactions.size() - 1)).getUuid());
                    assertNotNull(t);
                    assertNull(reader.readByKey(UUID.fromString("000-0000-0000-0000-000000000000")));
                }
        );

        assertDoesNotThrow(() -> {
            testFile.delete();
            testFile.createNewFile();
        });

        assertDoesNotThrow(() -> {
            Transaction t = reader.read();
            assertNull(t);
        });
    }

    @Test
    void testPruneDuplicatedEntry() throws FileNotFoundException {
        var ref = new Object() {
            Transaction t;
        };

        assertDoesNotThrow(() -> ref.t = reader.read());
        ModelWriter<Transaction> writer = new TransactionFileWriter(testFile);
        assertDoesNotThrow(writer::reset);
        assertDoesNotThrow(() -> writer.write(ref.t));
        assertDoesNotThrow(() -> writer.write(ref.t));

        assertDoesNotThrow(() -> assertNotNull(reader.readByKey(ref.t.getUuid())));

        int count = (int) reader.readAll()
                .stream()
                .filter(transaction -> transaction.getUuid().equals(ref.t.getUuid()))
                .count();

        assertEquals(1, count);
    }

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
            while (t.getUuid().toString().equals("000-0000-0000-0000-000000000000")) {
                t = new Transaction(rand.nextInt(100), rand.nextInt(5));
            }
            transactions.add(t);
        }
        return transactions;
    }
}