package io.dsub;

import java.util.Map;
import java.util.Optional;

/**
 * Where we set application configuration
 */
public class AppConfig {
    public final String BASE_DIR = getBaseDir();

    private static AppConfig instance;

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    };

    private static String getBaseDir() {
        return System.getProperty("user.dir");
    }
}
