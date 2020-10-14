package io.dsub.datasource;

import io.dsub.model.Transaction;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface TransactionReader {
    List<Transaction> readAll(URI uri) throws IOException;
    Transaction read(URI uri, UUID uuid) throws IOException;
}
