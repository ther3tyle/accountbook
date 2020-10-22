package io.dsub.repository;

import io.dsub.AppState;
import io.dsub.constants.StringConstants;
import io.dsub.model.Category;
import io.dsub.model.Model;
import io.dsub.util.QueryStringGenerator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

public abstract class JdbcModelRepository<V extends Model> implements ModelRepository<V> {
    protected final Connection conn;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final String schema;
    private final String table;

    public JdbcModelRepository(String schema, String table) throws SQLException {
        this(AppState.getInstance().getConn(), schema, table);
    }

    public JdbcModelRepository(Connection conn, String schema, String table) throws SQLException {
        this.conn = conn;
        this.schema = schema;
        this.table = table;
        if (isSchemaMissing()) {
            if (!initSchema()) {
                throw new ExceptionInInitializerError("failed to initialize");
            }
            conn.setSchema(StringConstants.SCHEMA);
            logger.info("initialized database");
        }
    }

    @Override
    public List<V> findAll() throws SQLException {
        String query = QueryStringGenerator.getInstance().getSelectQuery(schema, table) + " ORDER BY id";
        ResultSet rs = executeWithResultSet(query);
        return multiParse(rs);
    }

    @Override
    public V findById(String id) throws SQLException {
        String query = QueryStringGenerator.getInstance().getSelectQuery(schema, table, "WHERE id = " + String.format("'%s'", id));
        ResultSet resultSet = executeWithResultSet(query);
        return parse(resultSet);
    }

    @Override
    public void deleteById(String id) throws SQLException {
        String sql = QueryStringGenerator.getInstance().getDeleteQuery(schema, table, " WHERE id = " + id);
        conn.createStatement().execute(sql);
    }

    @Override
    public long count() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT('ID') AS \"COUNT\" FROM " + schema + "." + table);
        rs.next();
        return rs.getLong("COUNT");
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

        return conn.createStatement().execute(sqlString);
    }

    private boolean isSchemaMissing() throws SQLException {
        ResultSet rs = conn.getMetaData().getSchemas();
        while (rs.next()) {
            if (rs.getString(1).equalsIgnoreCase(StringConstants.SCHEMA)) {
                return false;
            }
        }
        return true;
    }

    protected ResultSet executeWithResultSet(String query) throws SQLException {
        return conn.createStatement().executeQuery(query);
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

        boolean success = isBatchSuccess(result);

        if (isBatchSuccess(result)) {
            conn.commit();
        } else {
            rollbackBatch(savepoint);
        }

        cleanupBatch(savepoint);
        return success;
    }

    private boolean isBatchSuccess(int[] result) {
        boolean success = true;

        for (int code : result) {
            if (code == Statement.EXECUTE_FAILED) {
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

    protected abstract V parse(ResultSet resultSet) throws SQLException;
    protected abstract List<V> multiParse(ResultSet resultSet) throws SQLException;
}
