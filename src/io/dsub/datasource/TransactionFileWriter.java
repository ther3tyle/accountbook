package io.dsub.datasource;

import io.dsub.model.Transaction;
import io.dsub.util.FileHandler;
import io.dsub.util.LocalDataType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * An implementation of ModelWriter.class that writes Transaction.class to a local file.
 */
public class TransactionFileWriter implements ModelWriter<Transaction> {

    // logger
    private static final Logger logger = Logger.getLogger("TransactionFileReader");

    // default path to be written
    private final Path source;

    // constructor
    public TransactionFileWriter() {
        this.source = FileHandler.getPath(LocalDataType.TRANSACTION);
    }

    public TransactionFileWriter(File file) {
        this.source = file.toPath();
    }

    /**
     * Convenient method for writing a transaction
     *
     * @param item a transaction
     * @throws IOException something went wrong
     */
    @Override
    public void write(Transaction item) throws IOException {
        if (item != null) {
            this.writeTo(source.toFile(), item);
        }
    }

    @Override
    public void reset() throws IOException {
        source.toFile().delete();
        source.toFile().createNewFile();
    }

    /**
     * Convenient method for writing multiple transactions
     *
     * @param items a transaction
     * @throws IOException something went wrong
     */
    @Override
    public void writeAll(Transaction[] items) throws IOException {
        this.writeTo(source.toFile(), items);
    }

    private void writeTo(File file, Transaction... items) throws IOException {
        this.writeTo(file, true, items);
    }

    /**
     * Overwrites existing file with given items
     * If no file exists, this will create new file to be written with given data.
     *
     * @param file  target file
     * @param items items to be written
     * @throws IOException something went wrong
     */
    public void overwrite(File file, Transaction... items) throws IOException {
        this.writeTo(file, false, items);
    }

    /**
     * Base method for writing transaction(s) to given file
     * The given transaction will be appended to the file, but they won't be overwritten.
     *
     * @param file     target file
     * @param isAppend will append or overwrite the file
     * @param items    transaction(s) to be written
     * @throws IOException something went wrong
     */
    private void writeTo(File file, boolean isAppend, Transaction... items) throws IOException {
        checkOrGenerateFile(file);
        FileWriter writer = new FileWriter(file, isAppend);

        // simply resets the file to blank state
        if (items != null && items.length > 0) {
            for (Transaction item : items) {
                writer.append(item.toString());
            }
        }
        writer.flush();
        writer.close();
    }

    private void checkOrGenerateFile(File file) throws IOException {
        if (!file.exists()) {
            logger.info("file not found. creating " + LocalDataType.TRANSACTION.getFileName() + "...");
            Files.createFile(file.toPath());
            logger.info("done");
        }
    }
}
