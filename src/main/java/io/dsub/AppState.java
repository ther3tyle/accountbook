package io.dsub;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;

/**
 * Where we set application configuration
 */
public class AppState {
    private static AppState instance;

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    @Getter
    @Setter
    private Connection conn;
}
