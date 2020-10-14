package io.dsub;

import io.dsub.data.TransactionFileReader;
import io.dsub.data.TransactionFileWriter;
import io.dsub.data.TransactionReader;
import io.dsub.data.TransactionWriter;
import io.dsub.model.Transaction;

import java.net.URI;

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
        reader.readAll(uri);
    }
}
