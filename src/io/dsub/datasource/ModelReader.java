package io.dsub.datasource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ModelReader<T, K> {
    T read() throws FileNotFoundException;
    T readByKey(K key) throws IOException;
    List<T> readAll() throws FileNotFoundException;
}
