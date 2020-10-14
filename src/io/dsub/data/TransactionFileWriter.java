package io.dsub.data;

import io.dsub.model.Transaction;
import io.dsub.util.DataType;
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

        Path path = Path.of(uri + "/" + DataType.TRANSACTION_FILE.getValue());

        if (Files.notExists(path)) {
            FileHandler.makeFile(path);
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.toFile(), true))) {
            String data = String.format("%s,%s,%s,%s\n",
                    transaction.getAmount(),
                    transaction.getVendorId(),
                    transaction.getTime(),
                    transaction.getUuid()
            );
            bufferedWriter.write(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
