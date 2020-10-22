package io.dsub.cui.menu;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuTest {

    void callMenu() {
        MainMenu mainMenu = new MainMenu();

        int input =1;
        int result = mainMenu.call();
        assertEquals(input,result);
    }
}