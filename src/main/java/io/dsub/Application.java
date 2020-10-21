package io.dsub;

import io.dsub.constants.Constants;
import io.dsub.util.Initializer;

import java.sql.*;
import java.util.logging.Logger;

/**
 * Application entry point
 * <p>
 * todo: impl menu interfacing
 */
public class Application {
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        try {
            Initializer.init("reset_schema.sql", Constants.CONN_STRING);

            // do operations below
            // ...
            // ...
            // ...


        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } finally {
            closeConn();
        }
    }

    private static void closeConn() {
        try {
            AppState.getInstance().getConn().close();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}
