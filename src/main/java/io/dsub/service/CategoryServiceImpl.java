package io.dsub.service;

import io.dsub.AppState;
import io.dsub.model.Category;
import io.dsub.repository.CategoryRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryServiceImpl() throws SQLException {
        this(new CategoryRepository(AppState.getInstance().getConn()));
    }

    /**
     * saves item to target table
     *
     * @param item to be saved
     * @return String typed key of item or null if failure
     */

    @Override
    public String save(Category item) {
        try {
            return this.repository.save(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveAll(Collection<Category> items) {
        try {
            repository.saveAll(items);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Category find(String name) {
        return findByName(name);
    }

    @Override
    public Category findByName(String name) {
        try {
            return repository.findByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category findById(String id) {
        try {
            return repository.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Category> findAll() {
        List<Category> items = new ArrayList<>();
        try {
            items.addAll(repository.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public void delete(Category item) {
        try {
            repository.delete(item);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            repository.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByName(String name) throws UnsupportedOperationException {
        try {
            repository.deleteByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll(Collection<Category> items) {
        try {
            for (Category item : items) {
                repository.delete(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
