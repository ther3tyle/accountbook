package io.dsub.data;

import io.dsub.model.Transaction;

import java.net.URI;

public interface ModelWriter {
    void write(URI url, Transaction transaction);
}
