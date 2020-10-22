package io.dsub.repository;

import io.dsub.constants.StringConstants;
import io.dsub.model.Transaction;
import io.dsub.util.QueryStringGenerator;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionRepository extends JdbcModelRepository<Transaction> {
    private final QueryStringGenerator queryGen = QueryStringGenerator.getInstance();

    public TransactionRepository() throws SQLException {
        super(StringConstants.SCHEMA, StringConstants.TRANSACTION);
    }

    public TransactionRepository(Connection conn) throws SQLException {
        super(conn, StringConstants.SCHEMA, StringConstants.TRANSACTION);
    }

    @Override
    public String save(Transaction item) throws SQLException {
        String query = queryGen.getInsertQuery(
                StringConstants.SCHEMA, StringConstants.TRANSACTION, getEntries(item)
        );

        conn.createStatement().executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

        return item.getId();
    }

    @Override
    public void saveAll(Collection<Transaction> items) throws SQLException {
        List<String> queries = items.stream()
                .map(item -> queryGen.getInsertQuery(
                        StringConstants.SCHEMA, StringConstants.TRANSACTION, getEntries(item)))
                .collect(Collectors.toList());
        executeBatchQuery(queries);
    }

    @Override
    public void delete(Transaction item) throws IOException, SQLException {
        Map<String, String> conditions = getEntries(item);
        String query = queryGen.getDeleteQuery(
                StringConstants.SCHEMA, StringConstants.TRANSACTION, conditions);
        conn.createStatement().execute(query);
    }

    @Override
    protected Transaction parse(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            UUID id = resultSet.getObject("ID", java.util.UUID.class);
            LocalDateTime time = resultSet.getObject("TIME", LocalDateTime.class);
            long amount = resultSet.getLong("AMOUNT");
            int vendorId = resultSet.getInt("VENDOR_ID");
            return new Transaction(amount, vendorId, time, id);
        }
        return null;
    }

    @Override
    protected List<Transaction> multiParse(ResultSet resultSet) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        while (resultSet.next()) {
            UUID id = resultSet.getObject("ID", java.util.UUID.class);
            LocalDateTime time = resultSet.getObject("TIME", LocalDateTime.class);
            long amount = resultSet.getLong("AMOUNT");
            int vendorId = resultSet.getInt("VENDOR_ID");
            transactions.add(new Transaction(amount, vendorId, time, id));
        }
        return transactions;
    }

    private Map<String, String> getEntries(Transaction item) {
        Map<String, String> pairs = new HashMap<>();

        pairs.put("AMOUNT", String.valueOf(item.getAmount()));
        pairs.put("VENDOR_ID", String.valueOf(item.getVendorId()));
        pairs.put("time", String.valueOf(Timestamp.valueOf(item.getTime())));
        if (item.getId() != null && !item.getId().equals("null")) {
            pairs.put("id", item.getId());
        }

        return pairs;
    }
}
