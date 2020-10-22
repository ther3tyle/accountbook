package io.dsub.cui.menu;

import io.dsub.cui.MenuType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputTransactionMenuTest {

    void callMenu() {
        InputTransactionMenu inputTransactionMenu = new InputTransactionMenu(MenuType.INCOME);

        int result = inputTransactionMenu.callMenu();
        assertEquals(0, result);
    }
}