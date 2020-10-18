package io.dsub.datasource;

import io.dsub.model.Model;

import java.io.IOException;
import java.util.List;

public interface ModelReader<T extends Model> {
    T read() throws IOException;
    T readByKey(String key) throws IOException;
    List<T> readAll() throws IOException;
}
