package io.dsub.cui.menu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuTest {

    @Test
    void callMenu() {
        MainMenu mainMenu = new MainMenu();

        int input =1;
        int result = mainMenu.callMenu();
        assertEquals(input,result);
    }
}