package io.dsub.repository;

import io.dsub.constants.StringConstants;
import io.dsub.model.Category;
import io.dsub.util.QueryStringGenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class CategoryRepository extends JdbcModelRepository<Category, Integer> {

    private final Logger logger = Logger.getLogger(CategoryRepository.class.getName());
    private final QueryStringGenerator queryGen = QueryStringGenerator.getInstance();
    private static final String SCHEMA = StringConstants.SCHEMA;
    private static final String TABLE = StringConstants.CATEGORY;

    public CategoryRepository() throws SQLException {
        super();
    }

    public CategoryRepository(Connection conn) throws SQLException {
        super(conn);
    }

    @Override
    public Category find(Integer id) throws SQLException {
        String query = queryGen.getSelectQuery(SCHEMA, TABLE, " WHERE id = " + id);
        ResultSet resultSet = super.executeWithResultSet(query);
        return parse(resultSet);
    }

    @Override
    public Category findByName(String name) throws SQLException {
        String query = queryGen.getSelectQuery(SCHEMA, TABLE, " WHERE name = " + name);
        ResultSet resultSet = executeWithResultSet(query);
        return parse(resultSet);
    }

    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> categories = new ArrayList<>();

        String query = queryGen.getSelectQuery(SCHEMA, TABLE) + " ORDER BY id";
        ResultSet rs = executeWithResultSet(query);

        while (rs.next()) {
            categories.add(parse(rs));
        }

        return categories;
    }

    @Override
    public void save(Category item) throws SQLException {
        Map<String, String> pair = new HashMap<>();
        pair.put("id", String.valueOf(item.getId()));
        pair.put("name", item.getName());

        String query = queryGen.getInsertQuery(SCHEMA, TABLE, pair);
        execute(query);
    }

    /**
     * writes all items to the target
     *
     * @param items to be written
     */
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

        boolean result = executeBatchQuery(queries);
        logger.info("batch process " + (result ? "success" : "fail"));
    }

    @Override
    public void delete(Category item) throws SQLException {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("id", String.valueOf(item.getId()));
        conditions.put("name", item.getName());
        String sql = queryGen.getDeleteQuery(SCHEMA, TABLE, conditions);
        execute(sql);
    }

    @Override
    public void deleteById(String id) throws SQLException {
        String sql = queryGen.getDeleteQuery(SCHEMA, TABLE, " WHERE id = " + id);
        execute(sql);
    }

    @Override
    public void deleteByName(String name) throws SQLException {
        String sql = queryGen.getDeleteQuery(SCHEMA, TABLE, " WHERE name = " + name);
        execute(sql);
    }

    private Category parse(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            return new Category(id, name);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return null;
        }
    }
}
