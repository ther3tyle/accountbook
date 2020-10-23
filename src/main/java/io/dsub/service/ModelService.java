package io.dsub.service;

import io.dsub.model.Model;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public interface ModelService<T extends Model> {
    /**
     * Updates or inserts item
     *
     * @param item to be saved
     * @return key of item in String
     */
    String save(T item) throws SQLException;

    /**
     * Updates or inserts items
     *
     * @param items to be saved
     */
    void saveAll(Collection<T> items);

    /**
     * Find item by name
     * If the given subclass did not implemented the method, it should throw {@link UnsupportedOperationException}
     *
     * @param name of item
     * @return item
     * @throws UnsupportedOperationException if unimplemented
     */
    T find(String name) throws UnsupportedOperationException;

    /**
     * Find all items by name
     * If the given subclass did not implemented the method, it should throw {@link UnsupportedOperationException}
     *
     * @param name of item
     * @return item or null if not found
     * @throws UnsupportedOperationException if unimplemented
     */
    T findByName(String name) throws UnsupportedOperationException;

    /**
     * Finds item by id
     *
     * @param id of item
     * @return item or null if not found
     */
    T findById(String id);

    /**
     * fetch all items as list (limit : 1000);
     *
     * @return items of type T
     */
    List<T> findAll();

    /**
     * Delete given item if exists
     * It will either delete or ignore depends on the existence of target
     *
     * @param item to be deleted
     */
    void delete(T item);

    /**
     * Delete item of id if exists
     * It will either delete or ignore depends on the existence of target
     *
     * @param id of item to be deleted
     */
    void deleteById(String id);

    /**
     * Deletes an item by name.
     * <p>
     * If the given subclass did not implemented the method, it should throw
     * {@link UnsupportedOperationException}
     *
     * @param name of target item
     * @throws UnsupportedOperationException if unimplemented
     */
    void deleteByName(String name) throws UnsupportedOperationException;

    /**
     * Delete all items if exists
     * It will either delete or ignore depends on the each existence of targets
     *
     * @param items to be deleted
     */
    void deleteAll(Collection<T> items);
}