package io.dsub.repository;

import java.io.IOException;
import java.sql.SQLException;
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
    T find(K key) throws IOException, SQLException;

    /**
     * reads all entities from target source
     *
     * @return list of entities
     */
    List<T> findAll() throws IOException, SQLException;

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

    /**
     * removes given item from the repository
     *
     * @param item to be removed
     */
    void delete(T item) throws IOException;

    /**
     * removes item with given key or id
     *
     * @param id to be removed
     */
    void deleteById(String id);
}
