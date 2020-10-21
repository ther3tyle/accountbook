package io.dsub.repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Base repository abstraction for models
 *
 * @param <T> type of entity
 * @since Oct 15, 2020
 * @author ther3tyle
 */
public interface ModelRepository<T>  {
    /**
     * read single item by key
     *
     * @param key key of entity
     * @return matching entity or null if not present
     */
    T findById(String key) throws IOException, SQLException;

    /**
     * read single item by name
     *
     * @param name name of entity
     * @return matching entity or null if not present
     * @throws UnsupportedOperationException if table does not have a column 'name'
     */
    default T findByName(String name) throws SQLException, UnsupportedOperationException {
        throw new UnsupportedOperationException(getClass().getName() + " does not implemented this operation");
    };

    /**
     * reads all entities from target source
     *
     * @return list of entities
     */
    Collection<T> findAll() throws IOException, SQLException;

    /**
     * writes single item to target source
     *
     * @param item item to be written
     * @return key of item in database
     */
    String save(T item) throws IOException, SQLException;

    /**
     * writes all items to the target
     *
     * @param items to be written
     */
    void saveAll(Collection<T> items) throws IOException, SQLException;

    /**
     * removes given item from the repository
     *
     * @param item to be removed
     */
    void delete(T item) throws IOException, SQLException;


    default void deleteByName(String name) throws SQLException, UnsupportedOperationException {
        throw new UnsupportedOperationException(getClass().getName() + " does not implemented this operation");
    }

    /**
     * removes item with given key or id
     *
     * @param id to be removed
     */
    void deleteById(String id) throws SQLException;

    long count() throws SQLException, IOException;
}
