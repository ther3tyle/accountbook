package io.dsub.repository;

import io.dsub.constants.Constants;
import io.dsub.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CategoryRepository extends JdbcModelRepository<Category, Integer> {
    private final Logger logger = Logger.getLogger(CategoryRepository.class.getName());

    public CategoryRepository() throws SQLException {
        super();
    }

    @Override
    public Category find(Integer key) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM " + Constants.CATEGORY + " WHERE id = " + key);
        return parse(rs);
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM account.category");

            while (rs.next()) {
                categories.add(parse(rs));
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return categories;
    }

    @Override
    public void write(Category item) {
        try {
            Statement statement = conn.createStatement();
            String insertQuery = getInsertQuery(item.getName());
            statement.execute(insertQuery);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void writeAll(Category[] items) {
        try {
            Statement statement = conn.createStatement();
            for (Category item : items) {
                statement.addBatch(getInsertQuery(item.getName()));
            }
            statement.executeBatch();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void delete(Category item) {
        String sql = getDeleteQuery(item.getName());
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = getDeleteQuery(id);
        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
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

    private String getInsertQuery(String name) {
        return "INSERT INTO account.category (name) VALUES ( '" + name + "' )";
    }

    private String getDeleteQuery(String name) {
        return "DELETE FROM ACCOUNT.CATEGORY WHERE NAME = '" + name + "'";
    }

    private String getUpdateQuery(Category item) {
        return "UPDATE account.category SET name = " + item.getName() + " WHERE id = " + item.getId();
    }
}
