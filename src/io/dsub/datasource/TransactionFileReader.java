package io.dsub.datasource;

import io.dsub.model.Transaction;
import io.dsub.util.FileHandler;

import java.io.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TransactionFileReader implements TransactionReader {
    private static final Logger logger = Logger.getLogger("TransactionFileReader");

    @Override
    public List<Transaction> readAll(URI uri) throws IOException {
        File file = FileHandler.readFile(uri);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            return reader.lines().map(this::parse).collect(Collectors.toList());
        } catch (IOException e) {
            logger.severe("IOException occurred: " + e.getMessage());
            throw e;
        }
    }

    public Transaction read(File file, UUID uuid) throws IOException {
        return this.read(file.toURI(), uuid);
    }

    @Override
    public Transaction read(URI uri, UUID uuid) throws IOException {

        File file = FileHandler.readFile(uri);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

            Optional<Transaction> optionalTransaction = reader.lines()
                    .filter(line -> line.split(",")[3].equals(uuid.toString()))
                    .findFirst()
                    .map(this::parse);

            if (optionalTransaction.isEmpty()) {
                throw new NoSuchElementException("failed to fetch transaction with UUID: " + uuid.toString());
            }

            return optionalTransaction.get();

        } catch (IOException e) {
            logger.severe("IOException occurred: " + e.getMessage());
            throw e;
        }
    }

    private Transaction parse(String inputLine) {
        String[] data = inputLine.split(",");
        return new Transaction(Integer.parseInt(data[0]), Integer.parseInt(data[1]), LocalDateTime.parse(data[2]), UUID.fromString(data[3]), false);
    }

}
