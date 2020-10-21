package io.dsub.repository;

import io.dsub.AppState;
import io.dsub.constants.Constants;
import io.dsub.model.Model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Collection;
import java.util.logging.Logger;

public abstract class JdbcModelRepository<V extends Model, K> implements ModelRepository<V, K> {
    protected final Connection conn;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public JdbcModelRepository() throws SQLException {
        this(AppState.getInstance().getConn());
    }

    public JdbcModelRepository(Connection conn) throws SQLException {
        this.conn = conn;
        if (isSchemaMissing()) {
            if (!initSchema()) {
                throw new ExceptionInInitializerError("failed to initialize");
            }
            conn.setSchema(Constants.SCHEMA);
            logger.info("initialized database");
        }
    }

    private boolean initSchema() throws SQLException {
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

    protected boolean execute(String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.execute(sql);
        } catch (SQLException e) {
            throw e;
        }
    }

    private boolean isSchemaMissing() throws SQLException {
        ResultSet rs = conn.getMetaData().getSchemas();
        while (rs.next()) {
            if (rs.getString(1).equalsIgnoreCase(Constants.SCHEMA)) {
                return false;
            }
        }
        return true;
    }

    protected ResultSet executeWithResultSet(String query) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(query);
        }
    }

    protected boolean executeBatchQuery(Collection<String> batch) throws SQLException {

        if (batch == null || batch.size() == 0) {
            return false;
        }

        Savepoint savepoint = prepareBatch();

        Statement stmt = conn.createStatement();
        stmt.closeOnCompletion();

        for (String query : batch) {
            stmt.addBatch(query);
        }

        int[] result = stmt.executeBatch();
        String[] queries = batch.toArray(String[]::new);

        boolean success = isBatchSuccess(result, queries);

        if (isBatchSuccess(result, queries)) {
            conn.commit();
        } else {
            rollbackBatch(savepoint);
        }

        cleanupBatch(savepoint);
        return success;
    }

    private boolean isBatchSuccess(int[] result, String[] queries) {
        boolean success = true;

        for (int i = 0; i < result.length; i++) {
            int code = result[i];
            String executedQuery = queries[i];
            if (code == Statement.EXECUTE_FAILED) {
                logger.severe("execution failed: [" + executedQuery + "]");
                success = false;
                break;
            }
        }

        return success;
    }

    private Savepoint prepareBatch() {
        Savepoint savepoint = null;
        try {
            savepoint = conn.setSavepoint();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
        return savepoint;
    }

    private void rollbackBatch(Savepoint savepoint) {
        try {
            if (savepoint != null) {
                conn.rollback(savepoint);
            }
            conn.releaseSavepoint(savepoint);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }

    private void cleanupBatch(Savepoint savepoint) {
        try {
            if (savepoint != null) {
                conn.releaseSavepoint(savepoint);
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }
    }
}
