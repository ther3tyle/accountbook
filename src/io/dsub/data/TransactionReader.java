package io.dsub.data;

import io.dsub.model.Transaction;

import java.net.URI;
import java.util.List;
import java.util.UUID;

public interface TransactionReader {
    List<Transaction> readAll(URI uri);
    Transaction read(URI uri, UUID uuid);
}
