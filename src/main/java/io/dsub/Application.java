package io.dsub;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Application entry point
 *
 * todo: impl menu interfacing
 */
public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        String H2_URL = "jdbc:h2:" + System.getProperty("user.dir") + File.separator + "db" + File.separator + "h2";

        try {
            System.out.println("Connecting to database...");
            Connection conn = DriverManager.getConnection(H2_URL, "sa", "");

            System.out.println("Creating table in given database...");
            Statement stmt = conn.createStatement();
            stmt.closeOnCompletion();

            InputStream input = Application.class.getClassLoader().getResourceAsStream("reset_schema.sql");

            if (input != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String sql = reader.lines().reduce("", (acc, curr) -> acc + curr);
                stmt.execute(sql);
            }
            conn.close();
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

    }
}
