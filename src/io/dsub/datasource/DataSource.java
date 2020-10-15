package io.dsub.datasource;

public interface DataSource<K, V> {
    void put(V value);
    V get(K key);
    void delete(K key);
    void update(K key, V value);
}
