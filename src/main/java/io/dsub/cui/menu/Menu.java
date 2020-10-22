package io.dsub.cui.menu;

public interface Menu {

    int callMenu();

    default int backToMainMenu() {
        return 0;
    }

}
