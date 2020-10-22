package io.dsub;

import io.dsub.constants.StringConstants;
import io.dsub.cui.MenuController;
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
    public static void main(String[] args) {
        try {
            // initialization phase
            Initializer.init("reset_schema.sql", StringConstants.CONN_STRING);

            // cui phase
            MenuController menuController = MenuController.getInstance();

            menuController.selectMenu();

        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
