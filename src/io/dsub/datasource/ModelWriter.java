package io.dsub.datasource;

import java.io.IOException;

public interface ModelWriter<T> {
    void write(T item) throws IOException;
    void writeAll(T[] items) throws IOException;
    void reset() throws IOException;
}
