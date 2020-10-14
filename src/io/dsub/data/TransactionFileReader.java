package io.dsub.data;

import io.dsub.model.Transaction;
import io.dsub.util.DataType;
import io.dsub.util.FileHandler;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionFileReader implements TransactionReader {
    @Override
    public List<Transaction> readAll(URI uri) {
        File file = FileHandler.readFile(uri);

        // todo: map string to Transaction
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            return reader.lines().map(this::parse).collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Oops... something went wrong");
        }
        return null;
    }

    private Transaction parse(String inputLine) {
        String[] data = inputLine.split(",");
        return new Transaction(Integer.parseInt(data[0]), Integer.parseInt(data[1]), LocalDateTime.parse(data[2]), UUID.fromString(data[3]), false);
    }

    @Override
    public Transaction read(URI uri, UUID uuid) {

        return null;
    }
}
