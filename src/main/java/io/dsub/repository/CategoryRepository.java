package io.dsub.repository;

import io.dsub.constants.StringConstants;
import io.dsub.model.Category;
import io.dsub.util.QueryStringGenerator;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Logger;

public class CategoryRepository extends JdbcModelRepository<Category> {

    private final Logger logger = Logger.getLogger(CategoryRepository.class.getName());
    private final QueryStringGenerator queryGen = QueryStringGenerator.getInstance();
    private static final String SCHEMA = StringConstants.SCHEMA;
    private static final String TABLE = StringConstants.CATEGORY;

    public CategoryRepository()  {
        super(SCHEMA, TABLE);
    }

    public CategoryRepository(Connection conn) {
        super(conn, SCHEMA, TABLE);
    }

    @Override
    public Category findByName(String targetName) throws SQLException {
        String query = queryGen.getSelectQuery(SCHEMA, TABLE, "WHERE name = '" + targetName + "'");
        ResultSet rs = executeWithResultSet(query);
        return parse(rs);
    }

    @Override
    public String save(Category item) throws SQLException {
        Map<String, String> pair = new HashMap<>();
        if (item.getId() != null) {
            pair.put("id", String.valueOf(item.getId()));
        }
        pair.put("name", item.getName());

        String query = queryGen.getInsertQuery(SCHEMA, TABLE, pair);
        int key = conn.createStatement().executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        return String.valueOf(key);
    }

    @Override
    public void saveAll(Collection<Category> items) throws SQLException {

        Set<String> queries = new HashSet<>();

        for (Category item : items) {
            Map<String, String> pair = new HashMap<>();
            pair.put("id", String.valueOf(item.getId()));
            pair.put("name", item.getName());

            String query = queryGen.getInsertQuery(SCHEMA, TABLE, pair);
            queries.add(query);
        }

        boolean success = executeBatchQuery(queries);
        if (!success) {
            throw new SQLException("failed batch operation");
        }
    }

    @Override
    public void delete(Category item) throws SQLException {
        Map<String, String> conditions = new HashMap<>();
        String id = item.getId();
        if (id != null && !id.equals("null")) {
            conditions.put("id", String.valueOf(item.getId()));
        }
        if (id != null && item.getName() == null) {
            throw new InvalidParameterException(String.format("incomplete model(id=%s name=%s)", item.getId(), item.getName()));
        }
        conditions.put("name", item.getName());
        String sql = queryGen.getDeleteQuery(SCHEMA, TABLE, conditions);
        conn.createStatement().execute(sql);
    }

    @Override
    public void deleteByName(String name) throws SQLException {
        String sql = queryGen.getDeleteQuery(SCHEMA, TABLE, " WHERE name = " + String.format("'%s'", name));
        conn.createStatement().execute(sql);
    }

    @Override
    protected Category parse(ResultSet rs) throws SQLException {
        if (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            return new Category(id, name);
        }
        return null;
    }

    @Override
    protected List<Category> multiParse(ResultSet resultSet) throws SQLException {
        List<Category> items = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            items.add(new Category(id, name));
        }
        return items;
    }
}
