package io.dsub.datasource;

import io.dsub.model.Model;
import io.dsub.util.DataType;
import io.dsub.util.FileHelper;
import io.dsub.util.ModelParserUtil;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ModelFileReader<T extends Model> implements LocalModelReader<T> {

    private Path sourcePath;
    private Function<String, Model> parser;

    public ModelFileReader(DataType type) {
        this(type, FileHelper.getPath(type));
    }

    public ModelFileReader(DataType type, File file) {
        this(type, file.toPath());
    }

    public ModelFileReader(DataType type, Path sourcePath) {
        this.sourcePath = sourcePath;
        this.parser = ModelParserUtil.get(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T pruneDuplicatedEntry(T item) throws IOException {
        List<T> itemsList = getReader()
                .lines()
                .map(this::parse)
                .filter(t -> !t.getId().equals(item.getId()))
                .collect(Collectors.toList());

        itemsList.add(item);

        ModelWriter<T> writer = new ModelFileWriter<>(DataType.TRANSACTION, sourcePath);

        if (itemsList.size() > 0) {
            writer.reset();
            writer.writeAll(itemsList);
        }

        return item;
    }

    @Override
    public BufferedReader getReader() throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(sourcePath.toFile())));
    }

    @Override
    @SuppressWarnings("unchecked")
    public T parse(String inputLine) {
        return (T) parser.apply(inputLine);
    }

    @Override
    public T read() throws IOException {
        String line = getReader().readLine();
        if (line == null || line.length() < 1) {
            throw new NoSuchElementException("file is empty");
        }
        return parse(getReader().readLine());
    }

    @Override
    public T readByKey(String key) throws IOException {
        BufferedReader reader = getReader();

        List<T> list = reader.lines()
                .map(this::parse)
                .filter(item -> item.getId().equalsIgnoreCase(key))
                .collect(Collectors.toList());

        if (list.size() > 1) {
            pruneDuplicatedEntry(list.get(0));
        }

        if (list.size() == 0) {
            throw new NoSuchElementException("failed to fetch item with " + key);
        }

        return list.get(0);
    }

    @Override
    public List<T> readAll() throws FileNotFoundException {
        BufferedReader reader = getReader();
        return reader.lines()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    @Override
    public Path getSourcePath() {
        return this.sourcePath;
    }

    @Override
    public void setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
    }
}
