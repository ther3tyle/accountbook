package io.dsub.datasource.writer;

import io.dsub.model.Model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface FlatFileWriter<T extends Model> extends ModelWriter<T> {
    void write(T item) throws IOException;

    void reset() throws IOException;

    void writeAll(T[] items) throws IOException;

    void writeTo(File file, T... items) throws IOException;

    void overwrite(File file, T... items) throws IOException;

    Path getSourcePath();

    void setSourcePath(Path path);
}
