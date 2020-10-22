package io.dsub.repository;

import io.dsub.Application;
import io.dsub.constants.DataType;
import io.dsub.constants.UIString;
import io.dsub.model.Vendor;
import io.dsub.util.QueryStringBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class VendorRepository extends JdbcModelRepository<Vendor> {
    private final QueryStringBuilder queryGen = QueryStringBuilder.getInstance();

    public VendorRepository() {
        super(Application.SCHEMA_NAME, DataType.VENDOR.getTableName());
    }

    public VendorRepository(Connection conn) {
        super(conn, Application.SCHEMA_NAME, DataType.VENDOR.getTableName());
    }

    @Override
    protected Vendor parse(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            int categoryId = resultSet.getInt("category_id");
            return new Vendor(id, name, categoryId);
        }
        return null;
    }

    @Override
    protected List<Vendor> multiParse(ResultSet resultSet) throws SQLException {
        List<Vendor> vendors = new ArrayList<>();
        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Integer categoryId = resultSet.getInt("category_id");
            vendors.add(new Vendor(id, name, categoryId));
        }
        return vendors;
    }

    @Override
    public Vendor findByName(String name) throws SQLException {
        String query = queryGen.getSelectQuery(
                Application.SCHEMA_NAME,
                DataType.VENDOR.getTableName(),
                "WHERE name = " + String.format("'%s'", name));
        ResultSet rs = conn.createStatement().executeQuery(query);
        return parse(rs);
    }

    @Override
    public String save(Vendor item) throws SQLException {
        String query = queryGen.getInsertQuery(
                Application.SCHEMA_NAME,
                DataType.VENDOR.getTableName(),
                getEntries(item));

        int key = conn.createStatement().executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        return String.valueOf(key);
    }

    @Override
    public void saveAll(Collection<Vendor> items) throws SQLException {
        List<String> queries = items.stream()
                .map(item -> queryGen.getInsertQuery(
                        Application.SCHEMA_NAME,
                        DataType.VENDOR.getTableName(),
                        getEntries(item)))
                .collect(Collectors.toList());
        executeBatchQuery(queries);
    }

    @Override
    public void delete(Vendor item) throws SQLException {
        String query = queryGen.getDeleteQuery(
                Application.SCHEMA_NAME,
                DataType.VENDOR.getTableName(),
                getEntries(item));
        conn.createStatement().execute(query);
    }

    @Override
    public void deleteByName(String name) throws SQLException {
        String query = queryGen.getDeleteQuery(
                Application.SCHEMA_NAME,
                DataType.VENDOR.getTableName(),
                "WHERE name = " + String.format("'%s'", name));
        conn.createStatement().execute(query);
    }

    private Map<String, String> getEntries(Vendor vendor) {
        Map<String, String> entries = new HashMap<>();

        if (vendor.getId() != null && !vendor.getId().equals("null")) {
            entries.put("id", vendor.getId());
        }

        if (vendor.getCatId() != null) {
            entries.put("category_id", String.valueOf(vendor.getCatId()));
        }

        entries.put("name", vendor.getName());
        return entries;
    }
}
