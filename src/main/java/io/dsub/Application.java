package io.dsub;

import io.dsub.cui.MenuController;

/**
 * Application entry point
 *
 * todo: impl menu interfacing
 */
public class Application {
    public static void main(String[] args) {

        MenuController menuController = new MenuController();
        menuController.selectMenu();
    }
}
