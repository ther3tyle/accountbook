package io.dsub;

import io.dsub.datasource.TransactionFileReader;
import io.dsub.datasource.TransactionFileWriter;
import io.dsub.datasource.TransactionReader;
import io.dsub.datasource.TransactionWriter;
import io.dsub.model.Transaction;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * Application entry point
 */
public class Application {
    public static void main(String[] args) {
        AppConfig config = AppConfig.getInstance();

        TransactionReader reader = new TransactionFileReader();
        TransactionWriter writer = new TransactionFileWriter();

        Transaction t1 = new Transaction(332, 4);
        Transaction t2 = new Transaction(12, 15);
        Transaction t3 = new Transaction(33422, 43);
        Transaction t4 = new Transaction(1238, 47);
        Transaction t5 = new Transaction(842, 1);
        Transaction t6 = new Transaction(554, 2);

        URI uri = URI.create(config.BASE_DIR);

        writer.write(uri, t1);
        writer.write(uri, t2);
        writer.write(uri, t3);
        writer.write(uri, t4);
        writer.write(uri, t5);
        writer.write(uri, t6);

        try {
            List<Transaction> transList = reader.readAll(uri);
            for (Transaction transaction : transList) {
                System.out.println(transaction.getUuid());
            }

        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}
