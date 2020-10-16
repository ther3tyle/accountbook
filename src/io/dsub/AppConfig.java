package io.dsub;

/**
 * Where we set application configuration
 */
public class AppConfig {
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
