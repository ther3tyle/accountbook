package io.dsub.service;

import io.dsub.AppState;
import io.dsub.model.Transaction;
import io.dsub.repository.TransactionRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    TransactionRepository repository;

    public TransactionServiceImpl() {
        this(new TransactionRepository(AppState.getInstance().getConn()));
    }

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public String save(Transaction item) {
        try {
            this.repository.save(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveAll(Collection<Transaction> items) {
        try {
            this.repository.saveAll(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transaction find(String name) {
        return this.findByName(name);
    }

    @Override
    public Transaction findByName(String name) {
        try {
            this.repository.findByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Transaction findById(String id) {
        try {
            this.repository.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transaction> findAll() {
        List<Transaction> items = new ArrayList<>();
        try {
            items.addAll(this.repository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public void delete(Transaction item) {
        try {
            this.repository.delete(item);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            this.repository.deleteById(id);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByName(String name) throws UnsupportedOperationException {
        try {
            this.repository.deleteByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll(Collection<Transaction> items) {
        try {
            for (Transaction item : items) {
                this.repository.delete(item);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
