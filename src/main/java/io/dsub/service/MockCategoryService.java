package io.dsub.service;

import io.dsub.model.Category;

import java.util.Collection;

public class MockCategoryService implements ModelService<Category> {
    @Override
    public void save(Category item) {

        System.out.println("CategorySave");
    }

    @Override
    public void saveAll(Collection<Category> items) {
        System.out.println("CategorySaveAll");

    }

    @Override
    public void delete(Category item) {
        System.out.println("CategoryDelete");
    }

    @Override
    public void deleteAll(Collection<Category> item) {
        System.out.println("CategoryDeleteAll");
    }
}
