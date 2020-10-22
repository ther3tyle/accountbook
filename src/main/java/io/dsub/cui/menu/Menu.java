package io.dsub.cui.menu;

// TODO: consider refactoring all child classes to be singleton
public interface Menu {

    int callMenu();

    default int backToMainMenu() {
        return 0;
    }

}
