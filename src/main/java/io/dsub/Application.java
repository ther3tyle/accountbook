package io.dsub;

import io.dsub.constants.StringConstants;
import io.dsub.util.Initializer;

import javax.naming.InsufficientResourcesException;
import java.io.FileNotFoundException;
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
            Initializer.init("reset_schema.sql", StringConstants.CONN_STRING);

            // do operations below
            // ...
            // ...
            // ...


        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InsufficientResourcesException e) {
            e.printStackTrace();
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
