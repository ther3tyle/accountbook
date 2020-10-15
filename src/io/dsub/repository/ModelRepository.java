package io.dsub.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Base repository abstraction for models
 *
 * @param <T> type of entity
 * @param <K> key of model
 * @since Oct 15, 2020
 * @author ther3tyle
 */
public interface ModelRepository<T, K>  {
    /**
     * read single item by Key
     *
     * @param key key of entity
     * @return matching entity or null if not present
     */
    T read(K key) throws IOException;

    /**
     * reads all entities from target source
     *
     * @return list of entities
     */
    List<T> readAll() throws FileNotFoundException;

    /**
     * writes single item to target source
     *
     * @param item item to be written
     */
    void write(T item) throws IOException;

    /**
     * writes all items to the target
     *
     * @param items to be written
     */
    void writeAll(T[] items) throws IOException;
}
