package io.dsub.datasource;

import io.dsub.model.Transaction;
import io.dsub.util.FileHandler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

public class TransactionFileWriter implements TransactionWriter {

    @Override
    public void write(URI uri, Transaction transaction) {

        Path path = Path.of(uri);

        if (Files.notExists(path)) {
            FileHandler.makeFile(path);
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.toFile(), true))) {
            bufferedWriter.write(transaction.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
