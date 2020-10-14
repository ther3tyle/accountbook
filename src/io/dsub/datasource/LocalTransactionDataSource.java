package io.dsub.datasource;

public class LocalTransactionDataSource<UUID, Transaction> implements DataSource <UUID, Transaction> {
    @Override
    public void put(Transaction value) {

    }

    @Override
    public Transaction get(UUID key) {
        return null;
    }

    @Override
    public void delete(UUID key) {

    }

    @Override
    public void update(UUID key, Transaction value) {

    }
}
