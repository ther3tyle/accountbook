package io.dsub.datasource;

import io.dsub.model.Transaction;

import java.net.URI;

public interface TransactionWriter extends ModelWriter {
    @Override
    void write(URI uri, Transaction transaction);
}