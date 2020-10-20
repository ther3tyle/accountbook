package io.dsub.repository;

import io.dsub.AppState;
import io.dsub.constants.Constants;
import io.dsub.model.Model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public abstract class JdbcModelRepository<V extends Model, K> implements ModelRepository<V, K> {
    protected final Connection conn = AppState.getInstance().getConn();
    private Logger logger = Logger.getLogger(JdbcModelRepository.class.getName());

    public JdbcModelRepository() throws SQLException {
        if (isSchemaMissing()) {
            if (!initSchema()) {
                throw new ExceptionInInitializerError("failed to initialize");
            }
            conn.setSchema(Constants.SCHEMA);
            logger.info("initialized database");
        }
    }

    private boolean initSchema() {
        InputStream sqlStream = getClass().getClassLoader().getResourceAsStream("reset_schema.sql");

        if (sqlStream == null) {
            return false;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(sqlStream));

        String sqlString = reader.lines()
                .reduce((acc, curr) -> acc + curr + "\n")
                .orElse("");

        if (sqlString.length() == 0) return false;

        return execute(sqlString);
    }

    private boolean execute(String sql) {
        try (Statement stmt = conn.createStatement()) {
            return stmt.execute(sql);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return false;
        }
    }

    private boolean isSchemaMissing() throws SQLException {
        ResultSet rs = conn.getMetaData().getSchemas();
        while(rs.next()) {
            if (rs.getString(1).equalsIgnoreCase(Constants.SCHEMA)) {
                return false;
            }
        }
        return true;
    }

    protected void setLogger(Logger logger) {
        this.logger = logger;
    }
}
