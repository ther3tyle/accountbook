package io.dsub.util;

import io.dsub.AppState;
import io.dsub.constants.StringConstants;
import io.dsub.datasource.AccountDataSource;

import javax.naming.InsufficientResourcesException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Initializer {

    private Initializer(){};

    private static final Logger LOGGER = Logger.getLogger(Initializer.class.getName());

    public static void init(String sqlFileName, String connectionString) throws SQLException, FileNotFoundException, InsufficientResourcesException {
        DataSource dataSource = new AccountDataSource(connectionString);
        Connection conn = dataSource.getConnection();
        AppState.getInstance().setConn(conn);
        if (isSchemaExists(conn)) {
            LOGGER.info(String.format("found schema %s. skipping initial schema creation...", StringConstants.SCHEMA));
        } else {
            LOGGER.info(String.format("failed to find %s. processing initial schema creation...", StringConstants.SCHEMA));
            initSchema(sqlFileName, conn);
            LOGGER.info("initialized schema " + StringConstants.SCHEMA);
        }
        LOGGER.info("validating...");
        boolean isValid = validateSchema(conn);
        if (!isValid) {
            LOGGER.severe("failed to validate schema! exiting...");
            System.exit(1);
        }
        LOGGER.info("validated schema.");
    }

    public static void initSchema(String sqlFileName, Connection conn) throws FileNotFoundException, SQLException, InsufficientResourcesException {
        try {
            String ddl = parseSchemaSQL(sqlFileName);
            conn.createStatement().execute(ddl);
        } catch (SQLException | InsufficientResourcesException | FileNotFoundException e) {
            LOGGER.severe(e.getMessage());
            throw e;
        }
    }

    private static boolean isSchemaExists(Connection conn) throws SQLException {
        try {
            ResultSet schemas = conn.getMetaData().getSchemas();
            boolean exists = false;
            while(schemas.next() && !exists) {
                exists = schemas.getString(1).equals(StringConstants.SCHEMA);
            }
            return exists;

        } catch (SQLException e) {
            Logger logger = Logger.getLogger(Initializer.class.getName());
            logger.severe(e.getMessage());
            throw e;
        }
    }

    private static String parseSchemaSQL(String resourceName) throws FileNotFoundException, InsufficientResourcesException {
        InputStream sqlStream = Initializer.class.getClassLoader().getResourceAsStream(resourceName);

        if (sqlStream == null) {
            throw new FileNotFoundException(resourceName + " not found");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(sqlStream));

        String sqlString = reader.lines()
                .reduce((acc, curr) -> acc + curr + "\n")
                .orElse("");

        if (sqlString.length() == 0) {
            throw new InsufficientResourcesException(resourceName + " is empty");
        }

        return sqlString;
    }

    private static boolean validateSchema(Connection conn) throws SQLException {
        if (!isSchemaExists(conn)) {
            LOGGER.severe(String.format("schema %s is missing\n", StringConstants.SCHEMA));
            return false;
        }

        Set<String> tables = new HashSet<>();
        tables.add(StringConstants.CATEGORY);
        tables.add(StringConstants.VENDOR);
        tables.add(StringConstants.TRANSACTION);

        return validateTables(conn, tables);
    }

    private static boolean validateTables(Connection conn, Collection<String> expectedTableNames) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getTables(null, StringConstants.SCHEMA, "%", null);

        while (rs.next()) {
            String tableName = rs.getString(3);
            expectedTableNames.remove(tableName);
        }

        return expectedTableNames.size() == 0;
    }
 }
