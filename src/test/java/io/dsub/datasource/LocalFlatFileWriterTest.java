package io.dsub.datasource;

import io.dsub.constants.DataType;
import io.dsub.datasource.writer.LocalFlatFileWriter;
import io.dsub.datasource.writer.ModelWriter;
import io.dsub.model.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LocalFlatFileWriterTest {

    private static ModelWriter<Transaction> writer;
    private static File testFile;

    @BeforeEach
    void prepTest() throws IOException {
        testFile = File.createTempFile("_test", "");
        writer = new LocalFlatFileWriter<>(DataType.TRANSACTION, testFile);
    }

    @AfterEach
    void cleanUp() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void write() {
        assertDoesNotThrow(() -> writer.reset());
        var ref = new Object() {
            BufferedReader reader;
        };
        assertDoesNotThrow(() -> ref.reader = new BufferedReader(new InputStreamReader(new FileInputStream(testFile))));

        assertDoesNotThrow(() -> populateData(writer, 100));
        assertEquals(100, ref.reader.lines().count());

        assertDoesNotThrow(() -> writer.write(null));
        assertEquals(0, ref.reader.lines().count());
    }

    @Test
    void testOverwrite() {
        assertDoesNotThrow(() -> populateData(writer, 50));
        var ref = new Object() {
            BufferedReader reader;
        };
        assertDoesNotThrow(() -> ref.reader = new BufferedReader(new InputStreamReader(new FileInputStream(testFile))));

        assertEquals(50, ref.reader.lines().count());

        assertDoesNotThrow(() -> writer.overwrite(testFile, new Transaction(1, 1)));

        assertDoesNotThrow(() -> ref.reader = new BufferedReader(new InputStreamReader(new FileInputStream(testFile))));
        assertEquals(1, ref.reader.lines().count());
    }

    @Test
    void reset() throws IOException {
        writer.reset();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(testFile)));
        populateData(writer, 85);

        assertEquals(85, reader.lines().count());

        writer.reset();
        assertEquals(0, reader.lines().count());
    }

    @Test
    void writeAll() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(testFile)));
        List<Transaction> transactions = new ArrayList<>();
        Random random = new Random();
        int count = random.nextInt(100);
        for (int i = 0; i < count; i++) {
            transactions.add(new Transaction(random.nextInt(100), random.nextInt(30)));
        }
        assertDoesNotThrow(() -> writer.writeAll(transactions.toArray(Transaction[]::new)));
        assertEquals(count, reader.lines().count());
    }

    @Test
    void testEmptyFile() throws IOException {
        testFile.delete();
        assertDoesNotThrow(() -> writer.write(new Transaction(1, 1)));
        assertTrue(testFile.exists());

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(testFile)));
        assertEquals(1, reader.lines().count());
    }

    private void populateData(ModelWriter<Transaction> writer, int count) throws IOException {
        Random random = new Random();
        int i = 0;
        while (i++ < count) {
            Transaction t = new Transaction(random.nextInt(100), random.nextInt(100));
            writer.write(t);
        }
    }
}