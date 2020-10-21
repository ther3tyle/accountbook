package io.dsub.util;

import io.dsub.AppState;
import io.dsub.datasource.AccountDataSource;

import javax.naming.InsufficientResourcesException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Initializer {

    private Initializer(){};

    private static final Logger LOGGER = Logger.getLogger(Initializer.class.getName());

    public static void init(String sqlFileName, String connectionString) throws SQLException {
        DataSource dataSource = new AccountDataSource(connectionString);
        Connection conn = dataSource.getConnection();
        AppState.getInstance().setConn(conn);
        initSchema(sqlFileName, conn);
    }

    public static void initSchema(String sqlFileName, Connection conn) {
        try {
            String ddl = parseSchemaSQL(sqlFileName);
            conn.createStatement().execute(ddl);
        } catch (SQLException | InsufficientResourcesException | FileNotFoundException e) {
            LOGGER.severe(e.getMessage());
            System.exit(1);
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
}
