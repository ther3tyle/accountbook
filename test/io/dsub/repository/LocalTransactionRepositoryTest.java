package io.dsub.repository;

import io.dsub.datasource.ModelReader;
import io.dsub.datasource.ModelWriter;
import io.dsub.datasource.TransactionFileReader;
import io.dsub.datasource.TransactionFileWriter;
import io.dsub.model.Transaction;
import io.dsub.util.FileHandler;
import io.dsub.util.LocalDataType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class LocalTransactionRepositoryTest {
    ModelRepository<Transaction, UUID> repository;

    @BeforeEach
    void setUp() {
        repository = new LocalTransactionRepository();
    }

    @AfterEach
    void cleanUp() {
        assertDoesNotThrow(() -> ((LocalTransactionRepository) repository).prune());
    }

    @AfterAll
    static void removeFile() {
        boolean res = FileHandler.getPath(LocalDataType.TRANSACTION).toFile().delete();
        if (!res) {
            Logger.getLogger(LocalTransactionRepositoryTest.class.getName())
                    .warning("failed to delete testFile");
        }
    }

    @Test
    void testConstructors() {
        LocalTransactionRepository repo = (LocalTransactionRepository) repository;

        assertNotNull(repo.getReader());
        assertNotNull(repo.getWriter());

        ModelWriter<Transaction> writer = repo.getWriter();
        ModelReader<Transaction, UUID> reader = repo.getReader();

        repo = new LocalTransactionRepository(writer);
        assertNotEquals(repo.getReader(), reader);
        assertEquals(repo.getWriter(), writer);

        reader = repo.getReader();
        writer = repo.getWriter();

        repo = new LocalTransactionRepository(reader);
        assertNotEquals(writer, repo.getWriter());
        assertEquals(reader, repo.getReader());

        reader = repo.getReader();
        writer = repo.getWriter();

        repo = new LocalTransactionRepository(reader, writer);
        assertEquals(reader, repo.getReader());
        assertEquals(writer, repo.getWriter());
    }

    @Test
    void read() {
        Transaction t = new Transaction(33, 33);
        assertDoesNotThrow(() -> repository.write(t));
        assertDoesNotThrow(() -> assertEquals(repository.read(t.getUuid()), t));

        Transaction other = new Transaction(33, 3);
        assertDoesNotThrow(() -> repository.write(other));
        assertDoesNotThrow(() -> assertEquals(repository.read(other.getUuid()), other));
        assertDoesNotThrow(() -> assertNotEquals(repository.read(other.getUuid()), t));
    }

    @Test
    void readAndWriteAll() {
        List<Transaction> list = new ArrayList<>();
        Random random = new Random();
        int randNum = random.nextInt(1000);
        for (int i = 0; i < randNum; i++) {
            list.add(new Transaction(random.nextInt(), random.nextInt()));
        }
        assertDoesNotThrow(() -> repository.writeAll(list.toArray(Transaction[]::new)));
        assertDoesNotThrow(() -> {
            int size = repository.readAll().size();
            assertEquals(size, list.size());
        });
    }

    @Test
    void write() {
        Random rand = new Random();
        Transaction transaction = new Transaction(rand.nextInt(100), rand.nextInt(100));
        assertDoesNotThrow(() -> repository.write(transaction));
        assertDoesNotThrow(() -> assertEquals(1, repository.readAll().size()));
    }

    @Test
    void setReader() {
        LocalTransactionRepository repo = (LocalTransactionRepository) repository;
        TransactionFileReader reader = new TransactionFileReader();
        TransactionFileReader old = (TransactionFileReader) repo.getReader();

        repo.setReader(reader);
        assertNotEquals(repo.getReader(), old);
    }

    @Test
    void setWriter() {
        LocalTransactionRepository repo = (LocalTransactionRepository) repository;
        TransactionFileWriter writer = new TransactionFileWriter();
        TransactionFileWriter old = (TransactionFileWriter) repo.getWriter();

        repo.setWriter(writer);
        assertNotEquals(repo.getWriter(), old);
    }

    @Test
    void prune() {
        LocalTransactionRepository repo = (LocalTransactionRepository) repository;
        assertDoesNotThrow(repo::prune);
        assertDoesNotThrow(() -> assertEquals(0, repo.readAll().size()));
    }
}