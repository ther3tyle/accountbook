package io.dsub.cui.menu;

import io.dsub.constants.MenuType;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputTransactionMenuTest {

    void callMenu() {
        InputTransactionMenu inputTransactionMenu = new InputTransactionMenu(MenuType.INCOME);

        int result = inputTransactionMenu.call();
        assertEquals(0, result);
    }
}