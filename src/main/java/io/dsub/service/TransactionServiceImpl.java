package io.dsub.service;

import io.dsub.model.Transaction;

import java.util.Collection;

public class TransactionServiceImpl implements TransactionService {


    @Override
    public String save(Transaction item) {
        return null;
    }

    @Override
    public void saveAll(Collection<Transaction> items) {

    }

    @Override
    public Transaction find(String name) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public Transaction findByName(String name) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public Transaction findById(String id) {
        return null;
    }

    @Override
    public void delete(Transaction item) {

    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void deleteByName(String name) throws UnsupportedOperationException {

    }

    @Override
    public void deleteAll(Collection<Transaction> items) {

    }
}
