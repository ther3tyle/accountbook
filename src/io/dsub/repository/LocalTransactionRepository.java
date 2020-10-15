package io.dsub.repository;

import io.dsub.datasource.ModelReader;
import io.dsub.datasource.ModelWriter;
import io.dsub.datasource.TransactionFileReader;
import io.dsub.datasource.TransactionFileWriter;
import io.dsub.model.Transaction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class LocalTransactionRepository implements ModelRepository<Transaction, UUID> {

    private ModelReader<Transaction, UUID> reader;
    private ModelWriter<Transaction> writer;

    public LocalTransactionRepository() {
        this(new TransactionFileReader(), new TransactionFileWriter());
    }

    public LocalTransactionRepository(ModelReader<Transaction, UUID> reader) {
        this(reader, new TransactionFileWriter());
    }

    public LocalTransactionRepository(ModelWriter<Transaction> writer) {
        this(new TransactionFileReader(), writer);
    }

    public LocalTransactionRepository(ModelReader<Transaction, UUID> reader, ModelWriter<Transaction> writer) {
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * read single item by Key
     *
     * @param key key of entity
     * @return matching entity or null if not present
     */
    @Override
    public Transaction read(UUID key) throws IOException {
        return reader.readByKey(key);
    }

    /**
     * Reads first instance of target entity from the source
     *
     * @return single instance
     */
    public Transaction read() throws FileNotFoundException {
        return reader.read();
    }

    /**
     * reads all entities from target source
     *
     * @return list of entities
     */
    @Override
    public List<Transaction> readAll() throws FileNotFoundException {
        return reader.readAll();
    }

    /**
     * writes single item to target source
     *
     * @param item item to be written
     */
    @Override
    public void write(Transaction item) throws IOException {
        writer.write(item);
    }

    /**
     * writes all items to the target
     *
     * @param items to be written
     */
    @Override
    public void writeAll(Transaction[] items) throws IOException {
        writer.writeAll(items);
    }

    public ModelReader<Transaction, UUID> getReader() {
        return reader;
    }

    public void setReader(ModelReader<Transaction, UUID> reader) {
        this.reader = reader;
    }

    public ModelWriter<Transaction> getWriter() {
        return writer;
    }

    public void setWriter(ModelWriter<Transaction> writer) {
        this.writer = writer;
    }

    public void prune() throws IOException {
        writer.reset();
    }
}
