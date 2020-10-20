package io.dsub;

import io.dsub.datasource.AccountDataSource;
import io.dsub.model.Category;
import io.dsub.repository.CategoryRepository;
import io.dsub.repository.JdbcModelRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Logger;

/**
 * Application entry point
 * <p>
 * todo: impl menu interfacing
 */
public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());
    private static final AppState appState = AppState.getInstance();

    public static void main(String[] args) {
        try {
            boolean initResult = init();
            if (!initResult) return;
            CategoryRepository categoryRepository = new CategoryRepository();
            categoryRepository.delete(new Category(-1, "hello"));
            categoryRepository.delete(new Category(-1, "world"));
            categoryRepository.delete(new Category(-1, "hi"));

            categoryRepository.write(new Category(-1, "hello"));
            categoryRepository.write(new Category(-1, "world"));
            categoryRepository.write(new Category(-1, "hi"));
            categoryRepository.findAll().forEach(System.out::println);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        } finally {
            closeConn();
        }
    }

    private static boolean init() {
        try {
            DataSource dataSource = new AccountDataSource();
            Connection conn = dataSource.getConnection();
            appState.setConn(conn);
            return true;
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            closeConn();
        }
        return false;
    }

    private static void closeConn() {
        try {
            appState.getConn().close();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }
}
