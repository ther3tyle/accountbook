package io.dsub.cui.menu;

import io.dsub.cui.InputHandler;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

// TODO: consider refactoring all child classes to be singleton
public interface Menu {
    InputHandler inputHandler = InputHandler.getInstance();
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
    int call();

    default void printHelp() {
        System.out.println("PRINTING...");
    }

    default int backToMainMenu() {
        return 0;
    }

}
