package io.dsub.repository;

import io.dsub.datasource.ModelFileReader;
import io.dsub.datasource.ModelFileWriter;
import io.dsub.datasource.ModelReader;
import io.dsub.datasource.ModelWriter;
import io.dsub.model.Model;
import io.dsub.util.DataType;
import io.dsub.util.FileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LocalModelRepository<T extends Model> implements ModelRepository<T, String> {
    private static final Logger logger = Logger.getLogger(LocalModelRepository.class.getName());

    private ModelReader<T> reader;
    private ModelWriter<T> writer;
    private final DataType dataType;
    private Path sourcePath;

    public LocalModelRepository(DataType type) {
        this(type, new ModelFileReader<>(type), new ModelFileWriter<>(type));
    }

    public LocalModelRepository(DataType type, Path path) {
        this(type, new ModelFileReader<>(type, path), new ModelFileWriter<>(type, path));
    }

    public LocalModelRepository(DataType type, File file) {
        this(type, new ModelFileReader<>(type, file), new ModelFileWriter<>(type, file));
    }

    private LocalModelRepository(DataType dataType, ModelReader<T> reader, ModelWriter<T> writer) {
        this.dataType = dataType;
        this.reader = reader;
        this.writer = writer;
        this.sourcePath = FileHelper.getPath(dataType);
    }

    /**
     * read single item by Key
     *
     * @param key key of entity
     * @return matching entity or null if not present
     */
    @Override
    public T read(String key) throws IOException {
        return reader.readByKey(key);
    }

    /**
     * Reads first instance of target entity from the source
     *
     * @return single instance
     */
    public T read() throws IOException {
        return reader.read();
    }

    /**
     * reads all entities from target source
     *
     * @return list of entities
     */
    @Override
    public List<T> readAll() throws IOException {
        return reader.readAll();
    }

    /**
     * writes single item to target source
     *
     * @param item item to be written
     */
    @Override
    public void write(T item) throws IOException {
        writer.write(item);
    }

    /**
     * writes all items to the target
     *
     * @param items to be written
     */
    @Override
    public void writeAll(T[] items) throws IOException {
        writer.writeAll(items);
    }

    /**
     * removes given item from the repository
     *
     * @param item to be removed
     */
    @Override
    public void delete(T item) {
        try {
            List<T> list = reader.readAll();
            list = list.stream()
                    .filter(entry -> entry.equals(item))
                    .collect(Collectors.toList());
            writer.reset();
            writer.writeAll(list);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * removes item with given key or id
     *
     * @param id to be removed
     */
    @Override
    public void deleteById(String id) {
        try {
            List<T> list = reader.readAll();
            list = list.stream()
                    .filter(entry -> entry.getId().equals(id))
                    .collect(Collectors.toList());
            writer.reset();
            writer.writeAll(list);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public ModelReader<T> getReader() {
        return reader;
    }

    public void setReader(ModelReader<T> reader) {
        this.reader = reader;
    }

    public ModelWriter<T> getWriter() {
        return writer;
    }

    public void setWriter(ModelWriter<T> writer) {
        this.writer = writer;
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
        this.writer = new ModelFileWriter<>(this.dataType, this.sourcePath);
        this.reader = new ModelFileReader<>(this.dataType, this.sourcePath);
    }

    public void prune() throws IOException {
        writer.reset();
    }
}
