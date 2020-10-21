package io.dsub.repository;

import io.dsub.constants.StringConstants;
import io.dsub.model.Vendor;
import io.dsub.util.QueryStringGenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class VendorRepository extends JdbcModelRepository<Vendor> {
    private final QueryStringGenerator queryGen = QueryStringGenerator.getInstance();

    public VendorRepository() throws SQLException {
        super(StringConstants.SCHEMA, StringConstants.VENDOR);
    }

    public VendorRepository(Connection conn) throws SQLException {
        super(conn, StringConstants.SCHEMA, StringConstants.VENDOR);
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
                StringConstants.SCHEMA,
                StringConstants.VENDOR,
                "WHERE name = " + String.format("'%s'", name));
        ResultSet rs = conn.createStatement().executeQuery(query);
        return parse(rs);
    }

    @Override
    public String save(Vendor item) throws SQLException {
        String query = queryGen.getInsertQuery(
                StringConstants.SCHEMA,
                StringConstants.VENDOR,
                getEntries(item));

        int key = conn.createStatement().executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        return String.valueOf(key);
    }

    @Override
    public void saveAll(Collection<Vendor> items) throws SQLException {
        List<String> queries = items.stream()
                .map(item -> queryGen.getInsertQuery(
                        StringConstants.SCHEMA,
                        StringConstants.VENDOR,
                        getEntries(item)))
                .collect(Collectors.toList());
        executeBatchQuery(queries);
    }

    @Override
    public void delete(Vendor item) throws SQLException {
        String query = queryGen.getDeleteQuery(
                StringConstants.SCHEMA,
                StringConstants.VENDOR,
                getEntries(item));
        conn.createStatement().execute(query);
    }

    @Override
    public void deleteByName(String name) throws SQLException {
        String query = queryGen.getDeleteQuery(
                StringConstants.SCHEMA,
                StringConstants.VENDOR,
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
