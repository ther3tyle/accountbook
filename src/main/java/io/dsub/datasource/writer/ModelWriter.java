package io.dsub.datasource.writer;

import io.dsub.model.Model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface ModelWriter<T extends Model> {
    void write(T item) throws IOException;

    void writeAll(T... items) throws IOException;

    void writeAll(Collection<T> items) throws IOException;

    void reset() throws IOException;

    void overwrite(File file, T... items) throws IOException;

    void overwrite(Collection<T> items) throws IOException;

    void overwrite(File file, Collection<T> items) throws IOException;
}

