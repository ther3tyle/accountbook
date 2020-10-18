package io.dsub.service;

import io.dsub.model.Model;

import java.util.Collection;

public interface ModelService <T extends Model> {
    void save(T item);
    void saveAll(Collection<T> items);
    void delete(T item);
    void deleteAll(Collection<T> item);
}