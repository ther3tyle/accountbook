package io.dsub.repository;

import io.dsub.Application;
import io.dsub.constants.DataType;
import io.dsub.constants.UIString;
import io.dsub.model.Transaction;
import io.dsub.util.QueryStringBuilder;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionRepository extends JdbcModelRepository<Transaction> {
    private final QueryStringBuilder queryGen = QueryStringBuilder.getInstance();

    public TransactionRepository() {
        super(Application.SCHEMA_NAME, DataType.TRANSACTION.getTableName());
    }

    public TransactionRepository(Connection conn) {
        super(conn, Application.SCHEMA_NAME, DataType.TRANSACTION.getTableName());
    }

    @Override
    public String save(Transaction item) throws SQLException {
        String query = queryGen.getInsertQuery(
                Application.SCHEMA_NAME, DataType.TRANSACTION.getTableName(), getEntries(item)
        );

        conn.createStatement().executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

        return item.getId();
    }

    @Override
    public void saveAll(Collection<Transaction> items) throws SQLException {
        List<String> queries = items.stream()
                .map(item -> queryGen.getInsertQuery(
                        Application.SCHEMA_NAME, DataType.TRANSACTION.getTableName(), getEntries(item)))
                .collect(Collectors.toList());
        executeBatchQuery(queries);
    }

    @Override
    public void delete(Transaction item) throws SQLException {
        Map<String, String> conditions = getEntries(item);
        String query = queryGen.getDeleteQuery(
                Application.SCHEMA_NAME, DataType.TRANSACTION.getTableName(), conditions);
        conn.createStatement().execute(query);
    }

    public List<Transaction> findBetween(LocalDate begin, LocalDate end) throws SQLException {
        String schemaTable = Application.SCHEMA_NAME + "." + DataType.TRANSACTION.getTableName();

        Statement stmt = conn.createStatement();
        String beginStr = String.format("'%s'", begin.toString());
        String endStr = String.format("'%s'", end.toString());
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + schemaTable + " WHERE DATE BETWEEN " + beginStr + " AND " + endStr);

        return multiParse(rs);
    }

    @Override
    protected Transaction parse(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            UUID id = resultSet.getObject("ID", java.util.UUID.class);
            LocalDate date = resultSet.getObject("DATE", LocalDate.class);
            long amount = resultSet.getLong("AMOUNT");
            int vendorId = resultSet.getInt("VENDOR_ID");
            return new Transaction(amount, vendorId, date, id);
        }
        return null;
    }

    @Override
    protected List<Transaction> multiParse(ResultSet resultSet) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        while (resultSet.next()) {
            UUID id = resultSet.getObject("ID", java.util.UUID.class);
            LocalDate date = resultSet.getObject("DATE", LocalDate.class);
            long amount = resultSet.getLong("AMOUNT");
            int vendorId = resultSet.getInt("VENDOR_ID");
            transactions.add(new Transaction(amount, vendorId, date, id));
        }
        return transactions;
    }

    private Map<String, String> getEntries(Transaction item) {
        Map<String, String> pairs = new HashMap<>();

        pairs.put("AMOUNT", String.valueOf(item.getAmount()));
        pairs.put("VENDOR_ID", String.valueOf(item.getVendorId()));
        pairs.put("DATE", String.valueOf(item.getDate()));
        if (item.getId() != null && !item.getId().equals("null")) {
            pairs.put("id", item.getId());
        }

        return pairs;
    }
}
