package io.dsub.service;

import io.dsub.model.Transaction;

import java.util.Collection;

public class MockTransactionService implements ModelService<Transaction> {
    @Override
    public String save(Transaction item) {
        return "거래기록완료";
    }

    @Override
    public void saveAll(Collection<Transaction> items) {
        System.out.println("TransactionSaveAll");
    }

    /**
     * Find item by name
     * If the given subclass did not implemented the method, it should throw {@link UnsupportedOperationException}
     *
     * @param name of item
     * @return item
     * @throws UnsupportedOperationException if unimplemented
     */
    @Override
    public Transaction find(String name) throws UnsupportedOperationException {
        return null;
    }

    /**
     * Find all items by name
     * If the given subclass did not implemented the method, it should throw {@link UnsupportedOperationException}
     *
     * @param name of item
     * @return item or null if not found
     * @throws UnsupportedOperationException if unimplemented
     */
    @Override
    public Transaction findByName(String name) throws UnsupportedOperationException {
        return null;
    }

    /**
     * Finds item by id
     *
     * @param id of item
     * @return item or null if not found
     */
    @Override
    public Transaction findById(String id) {
        return null;
    }

    @Override
    public void delete(Transaction item) {
        System.out.println("TransactionSaveDelete");
    }

    /**
     * Delete item of id if exists
     * It will either delete or ignore depends on the existence of target
     *
     * @param id of item to be deleted
     */
    @Override
    public void deleteById(String id) {

    }

    /**
     * Deletes an item by name.
     * <p>
     * If the given subclass did not implemented the method, it should throw
     * {@link UnsupportedOperationException}
     *
     * @param name of target item
     * @throws UnsupportedOperationException if unimplemented
     */
    @Override
    public void deleteByName(String name) throws UnsupportedOperationException {

    }

    @Override
    public void deleteAll(Collection<Transaction> item) {
        System.out.println("TransactionDeleteAll");
    }
}
