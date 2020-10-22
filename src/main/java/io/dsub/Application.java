package io.dsub;

import io.dsub.cui.MenuController;

/**
 * Application entry point
 *
 * TODO: impl menu interfacing
 */
public class Application {
    public static void main(String[] args) {
        MenuController menuController = MenuController.getInstance();

        // do everything you need to do before start cui.

        // ... initializers

        // begin cui
        menuController.selectMenu();
    }
}
