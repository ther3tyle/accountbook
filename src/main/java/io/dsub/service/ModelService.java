package io.dsub.service;

import io.dsub.model.Model;

import java.util.Collection;

public interface ModelService <T extends Model> {
    /**
     * Updates or inserts item
     *
     * @param item to be saved
     */
    void save(T item);

    /**
     * Updates or inserts items
     *
     * @param items to be saved
     */
    void saveAll(Collection<T> items);

    /**
     * Find item by name
     * If the given subclass did not implemented the method, it should throw {@UnsupportedOperationException}
     *
     * @param name of item
     * @return item
     * @throws UnsupportedOperationException
     */
    T find(String name) throws UnsupportedOperationException;

    /**
     * Find all items by name
     * If the given subclass did not implemented the method, it should throw {@UnsupportedOperationException}
     *
     * @param name of item
     * @return item
     * @throws UnsupportedOperationException
     */
    Collection<T> findAllByName(String name) throws UnsupportedOperationException;

    /**
     * Delete given item if exists
     * It will either delete or ignore depends on the existence of target
     *
     * @param item targetItem
     */
    void delete(T item);

    /**
     * Delete all items if exists
     * It will either delete or ignore depends on the each existence of targets
     *
     * @param items targetItems
     */
    void deleteAll(Collection<T> items);

    /**
     * Deletes an item by name.
     * If the given subclass did not implemented the method, it should throw {@UnsupportedOperationException}
     *
     * @param name of target item
     * @throws UnsupportedOperationException
     */
    void deleteByName(String name) throws UnsupportedOperationException;
}