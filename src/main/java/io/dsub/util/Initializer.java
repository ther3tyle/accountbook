package io.dsub.util;

import io.dsub.AppState;
import io.dsub.Application;
import io.dsub.datasource.AccountDataSource;

import javax.naming.InsufficientResourcesException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.logging.Logger;

public class Initializer {

    private Initializer(){}

    public static void init() throws FileNotFoundException, SQLException, InsufficientResourcesException {
        init(Application.RESET_SCHEMA_SQL, Application.PROD_CONN_STR);
    }

    public static void init(String sqlFileName, String connectionString) throws SQLException, FileNotFoundException, InsufficientResourcesException {
        DataSource dataSource = new AccountDataSource(connectionString);
        Connection conn = dataSource.getConnection();
        AppState.getInstance().setConn(conn);

        if (!DatabaseUtil.isSchemaExists(conn)) {
            DatabaseUtil.initSchema(sqlFileName, conn);
        }

        boolean isValid = DatabaseUtil.validateSchema(conn);

        if (!isValid) {
            throw new SQLException("failed to validate schema");
        }
    }
 }
