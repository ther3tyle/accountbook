package io.dsub.cui;

import io.dsub.constants.MenuType;
import io.dsub.cui.menu.*;

public class MenuController {

    private final Menu ADD_CATEGORY = new AddCategoryMenu();
    private final Menu CHECK_ACCOUNT = new ViewRecordMenu();
    private final Menu INITIALIZATION = new InitializationMenu();
    private final Menu INPUT_INCOME = new InputTransactionMenu(MenuType.INCOME);
    private final Menu INPUT_EXPENSE = new InputTransactionMenu(MenuType.EXPENSE);
    private final Menu MAIN_MENU = new MainMenu();
    private final Menu EXIT = new Exit();

    private MenuController() {}

    private static final MenuController INSTANCE = new MenuController();

    public static MenuController getInstance() {
        return INSTANCE;
    }

    public void selectMenu() {

        Menu menu = MAIN_MENU;
        int input = 0;

        while (input >= 0) {
            switch (input) {
                case 0 -> menu = MAIN_MENU;
                case 1 -> menu = INPUT_INCOME;
                case 2 -> menu = INPUT_EXPENSE;
                case 3 -> menu = CHECK_ACCOUNT;
                case 4 -> menu = ADD_CATEGORY;
                case 5 -> menu = INITIALIZATION;
                case 6 -> menu = EXIT;
            }
            input = menu.call();
        }
    }
}
