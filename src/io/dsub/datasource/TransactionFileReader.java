package io.dsub.datasource;

import io.dsub.model.Transaction;
import io.dsub.util.FileHandler;
import io.dsub.util.LocalDataType;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;


/**
 * FileReader concrete class for Transaction.class
 */
public class TransactionFileReader implements ModelReader<Transaction, UUID> {
    private static final Logger logger = Logger.getLogger("TransactionFileReader");

    private Path source;

    public TransactionFileReader() {
        this.source = FileHandler.getPath(LocalDataType.TRANSACTION);
    }

    public TransactionFileReader(Path path) {
        this.source = path;
    }

    @Override
    public Transaction read() throws FileNotFoundException {
        Optional<Transaction> optional = getReader().lines().findFirst().map(this::parse);
        if (optional.isEmpty()) {
            logger.warning("local file seems to be empty. returning null");
            return null;
        }
        return optional.get();
    }

    @Override
    public Transaction readByKey(UUID key) throws IOException {
        List<Transaction> transList = getReader()
                .lines()
                .filter(line -> parse(line).getUuid().equals(key))
                .map(this::parse)
                .collect(Collectors.toList());

        if (transList.size() > 1) {
            logger.info("duplicated entry found. pruning...");
            Transaction pruneTarget = transList.get(0);
            return this.pruneDuplicatedEntry(pruneTarget);
        }

        if (transList.size() == 0) {
            logger.info(String.format("transaction of id [%s] not found", key));
            return null;
        }

        return transList.get(0);
    }

    @Override
    public List<Transaction> readAll() throws FileNotFoundException {
        return getReader()
                .lines()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    private Transaction pruneDuplicatedEntry(Transaction transaction) throws IOException {
        List<Transaction> transList = getReader()
                .lines()
                .map(this::parse)
                .filter(t -> !t.getUuid().equals(transaction.getUuid()))
                .collect(Collectors.toList());

        transList.add(transaction);

        ModelWriter<Transaction> writer = new TransactionFileWriter(source.toFile());
        writer.reset();

        if (transList.size() > 0) {
            writer.writeAll(transList.toArray(Transaction[]::new));
        }

        return transaction;
    }

    public BufferedReader getReader() throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(source.toFile())));
    }

    private Transaction parse(String inputLine) {
        String[] data = inputLine.split(",");
        return new Transaction(
                Integer.parseInt(data[0]),
                Integer.parseInt(data[1]),
                LocalDateTime.parse(data[2]),
                UUID.fromString(data[3]),
                false
        );
    }

    public Path getSource() {
        return source;
    }

    public void setSource(Path source) {
        this.source = source;
    }
}
