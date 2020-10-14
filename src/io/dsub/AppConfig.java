package io.dsub;

import io.dsub.data.TransactionFileReader;
import io.dsub.data.TransactionFileWriter;
import io.dsub.model.Transaction;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
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
        Optional<Map.Entry<Object, Object>> optionalEntry = System.getProperties()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().equals("java.class.path"))
                .findFirst();

        if (optionalEntry.isPresent()) {
            Map.Entry<Object, Object> entry = optionalEntry.get();
            String val = (String) entry.getValue();
            val = val.split(":")[0];
            return val;
        }

        return "";
    }
}
