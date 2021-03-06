package io.dsub.repository;

import io.dsub.datasource.reader.LocalFlatFileReader;
import io.dsub.datasource.writer.LocalFlatFileWriter;
import io.dsub.datasource.reader.ModelReader;
import io.dsub.datasource.writer.ModelWriter;
import io.dsub.model.Model;
import io.dsub.constants.DataType;
import io.dsub.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LocalModelRepository<T extends Model> implements ModelRepository<T> {
    private static final Logger LOGGER = Logger.getLogger(LocalModelRepository.class.getName());

    private ModelReader<T> reader;
    private ModelWriter<T> writer;
    private final DataType dataType;
    private Path sourcePath;

    public LocalModelRepository(DataType type) {
        this(type, new LocalFlatFileReader<>(type), new LocalFlatFileWriter<>(type));
    }

    public LocalModelRepository(DataType type, Path path) {
        this(type, new LocalFlatFileReader<>(type, path), new LocalFlatFileWriter<>(type, path));
    }

    public LocalModelRepository(DataType type, File file) {
        this(type, new LocalFlatFileReader<>(type, file), new LocalFlatFileWriter<>(type, file));
    }

    private LocalModelRepository(DataType dataType, ModelReader<T> reader, ModelWriter<T> writer) {
        this.dataType = dataType;
        this.reader = reader;
        this.writer = writer;
        this.sourcePath = FileUtil.getPath(dataType);
    }

    /**
     * read single item by Key
     *
     * @param key key of entity
     * @return matching entity or null if not present
     */
    @Override
    public T findById(String key) throws IOException {
        return reader.readByKey(key);
    }

    /**
     * Reads first instance of target entity from the source
     *
     * @return single instance or null if not present
     */
    public T read() throws IOException {
        try {
            return reader.read();
        } catch (NoSuchElementException ignored) {
            return null;
        }
    }

    /**
     * reads all entities from target source
     *
     * @return list of entities
     */
    @Override
    public List<T> findAll() throws IOException {
        return reader.readAll();
    }

    /**
     * writes single item to target source
     *
     * @param item item to be written
     * @return null as is not supported yet
     */

    @Override
    public String save(T item) throws IOException {
        deleteById(item.getId());
        writer.write(item);
        return null; // TODO: return row value
    }

    /**
     * writes all items to the target
     *
     * @param items to be written
     */
    @Override
    public void saveAll(Collection<T> items) throws IOException {
        items.forEach(item -> deleteById(item.getId()));
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
            LOGGER.severe(e.getMessage());
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
            LOGGER.severe(e.getMessage());
        }
    }

    @Override
    public long count() throws IOException {
        return reader.readAll().size();
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
        this.writer = new LocalFlatFileWriter<>(this.dataType, this.sourcePath);
        this.reader = new LocalFlatFileReader<>(this.dataType, this.sourcePath);
    }

    public void prune() throws IOException {
        writer.reset();
    }
}
