package io.dsub.service;

import io.dsub.model.Transaction;

import java.util.Collection;

public class MockTransactionService implements ModelService<Transaction> {
    @Override
    public void save(Transaction item) {
        System.out.println("TransactionSave");
    }

    @Override
    public void saveAll(Collection<Transaction> items) {
        System.out.println("TransactionSaveAll");
    }

    @Override
    public void delete(Transaction item) {
        System.out.println("TransactionSaveDelete");
    }

    @Override
    public void deleteAll(Collection<Transaction> item) {
        System.out.println("TransactionDeleteAll");
    }
}
