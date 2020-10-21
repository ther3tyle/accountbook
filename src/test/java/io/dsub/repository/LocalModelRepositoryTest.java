package io.dsub.repository;

import io.dsub.datasource.reader.LocalFlatFileReader;
import io.dsub.datasource.writer.LocalFlatFileWriter;
import io.dsub.datasource.reader.ModelReader;
import io.dsub.datasource.writer.ModelWriter;
import io.dsub.model.Transaction;
import io.dsub.constants.DataType;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class LocalModelRepositoryTest {
    ModelRepository<Transaction, String> repository;
    static File testFile;

    @BeforeEach
    void setUp() {
        assertDoesNotThrow(() -> testFile = File.createTempFile("_test", ""));
        repository = new LocalModelRepository<>(DataType.TRANSACTION, testFile);
    }

    @AfterEach
    void cleanUp() {
        boolean res = testFile.delete();
        if (!res) {
            Logger.getLogger(LocalModelRepositoryTest.class.getName())
                    .warning("failed to delete testFile");
        }
    }

    @Test
    void testConstructors() {
        LocalModelRepository<Transaction> repo = (LocalModelRepository<Transaction>) repository;
        assertNotNull(repo.getReader());
        assertNotNull(repo.getWriter());
    }

    @Test
    void read() {
        Transaction t = new Transaction(33, 33);
        assertDoesNotThrow(() -> repository.save(t));
        assertDoesNotThrow(() -> assertEquals(repository.find(t.getId()), t));

        Transaction other = new Transaction(33, 3);
        assertDoesNotThrow(() -> repository.save(other));
        assertDoesNotThrow(() -> assertEquals(repository.find(other.getId()), other));
        assertDoesNotThrow(() -> assertNotEquals(repository.find(other.getId()), t));
    }

    @Test
    void readAndWriteAll() {
        List<Transaction> list = new ArrayList<>();
        Random random = new Random();
        int randNum = random.nextInt(1000);
        for (int i = 0; i < randNum; i++) {
            list.add(new Transaction(random.nextInt(), random.nextInt()));
        }
        assertDoesNotThrow(() -> repository.saveAll(list));
        assertDoesNotThrow(() -> {
            int size = repository.findAll().size();
            assertEquals(size, list.size());
        });
    }

    @Test
    void write() {
        Random rand = new Random();
        Transaction transaction = new Transaction(rand.nextInt(100), rand.nextInt(100));
        assertDoesNotThrow(() -> repository.save(transaction));
        assertDoesNotThrow(() -> assertEquals(1, repository.findAll().size()));
    }

    @Test
    void setReader() {
        LocalModelRepository<Transaction> repo = (LocalModelRepository<Transaction>) repository;
        ModelReader<Transaction> reader = new LocalFlatFileReader<>(DataType.TRANSACTION);
        ModelReader<Transaction> old = repo.getReader();

        repo.setReader(reader);
        assertNotEquals(repo.getReader(), old);
    }

    @Test
    void setWriter() {
        LocalModelRepository<Transaction> repo = (LocalModelRepository<Transaction>) repository;
        LocalFlatFileWriter<Transaction> writer = new LocalFlatFileWriter<>(DataType.TRANSACTION);
        ModelWriter<Transaction> old = repo.getWriter();

        repo.setWriter(writer);
        assertNotEquals(repo.getWriter(), old);
    }

    @Test
    void setSourcePath() {
        assertDoesNotThrow(() -> {
            LocalModelRepository<Transaction> repo = (LocalModelRepository<Transaction>) repository;
            Path backupPath = repo.getSourcePath();
            Path otherPath = File.createTempFile("_test", "tmp").toPath();
            otherPath.toFile().createNewFile();
            otherPath.toFile().deleteOnExit();

            assertNotNull(otherPath);

            repo.setSourcePath(otherPath);

            assertTrue(otherPath.toFile().exists());
            assertEquals(((LocalFlatFileReader<Transaction>)repo.getReader()).getSourcePath(),
                    ((LocalFlatFileWriter<Transaction>)repo.getWriter()).getSourcePath()
                    );
            assertEquals(otherPath, repo.getSourcePath());

            repo.setSourcePath(backupPath);
        });
    }

    @Test
    void prune() {
        LocalModelRepository<Transaction> repo = (LocalModelRepository<Transaction>) repository;
        assertDoesNotThrow(repo::prune);
        assertDoesNotThrow(() -> assertEquals(0, repo.findAll().size()));
    }
}