package io.dsub.util;

import io.dsub.Application;
import io.dsub.constants.DataType;

import javax.naming.InsufficientResourcesException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class DatabaseUtil {
    private DatabaseUtil() {
    }

    public static boolean isSchemaExists(Connection conn) throws SQLException {
        try {
            ResultSet schemas = conn.getMetaData().getSchemas();
            boolean exists = false;
            while (schemas.next() && !exists) {
                String name = schemas.getString(1);
                exists = name.equalsIgnoreCase(Application.SCHEMA_NAME);
            }
            return exists;

        } catch (SQLException e) {
            Logger logger = Logger.getLogger(Initializer.class.getName());
            logger.severe(e.getMessage());
            throw e;
        }
    }

    public static boolean validateSchema(Connection conn) throws SQLException {
        if (!isSchemaExists(conn)) {
            throw new SQLException("missing expected schema : " + Application.SCHEMA_NAME);
        }

        Set<String> tables = new HashSet<>();
        tables.add(DataType.CATEGORY.getTableName());
        tables.add(DataType.VENDOR.getTableName());
        tables.add(DataType.TRANSACTION.getTableName());

        return validateTables(conn, tables);
    }

    public static boolean validateTables(Connection conn, Collection<String> expectedTableNames) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getTables(null, Application.SCHEMA_NAME, "%", null);

        while (rs.next()) {
            String tableName = rs.getString(3).toUpperCase();
            expectedTableNames.remove(tableName);
        }

        return expectedTableNames.size() == 0;
    }

    public static String parseSchemaSQL(String resourceName) throws FileNotFoundException, InsufficientResourcesException {
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

    public static void initSchema(String sqlFileName, Connection conn) throws FileNotFoundException, SQLException, InsufficientResourcesException {
        try {
            String ddl = DatabaseUtil.parseSchemaSQL(sqlFileName);
            conn.createStatement().execute(ddl);
        } catch (SQLException | InsufficientResourcesException | FileNotFoundException e) {
            throw e;
        }
    }
}
