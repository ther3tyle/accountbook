package io.dsub.datasource.reader;

import io.dsub.model.Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FlatFileReader<T extends Model> extends ModelReader<T> {

    @Override
    T read() throws IOException;

    @Override
    T readByKey(String key) throws IOException;

    @Override
    List<T> readAll() throws FileNotFoundException;

    T parse(String inputLine);

    T pruneDuplicatedEntry(T item) throws IOException;

    BufferedReader getReader() throws FileNotFoundException;

    Path getSourcePath();

    void setSourcePath(Path sourcePath);
}
