package io.dsub.cui;

import io.dsub.cui.menu.*;

import java.sql.SQLException;

public class MenuController {

    private MenuController() throws SQLException {}

    private static final MenuController INSTANCE = null;

    static {
        try {
            INSTANCE = new MenuController();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static MenuController getInstance() {
        return INSTANCE;
    }

    private final Menu ADD_CATEGORY = new AddCategoryMenu();
    private final Menu CHECK_ACCOUNT = new CheckAccountMenu();
    private final Menu INITIALIZATION = new InitializationMenu();
    private final Menu INPUT_INCOME = new InputTransactionMenu(MenuType.INCOME);
    private final Menu INPUT_EXPENSE = new InputTransactionMenu(MenuType.EXPENSE);
    private final Menu MAIN_MENU = new MainMenu();


    public void selectMenu() {

        Menu menu = MAIN_MENU;
        int input = 0;

        while (true) {

            switch (input) {
                case 0 -> menu = MAIN_MENU;
                case 1 -> menu = INPUT_INCOME;
                case 2 -> menu = INPUT_EXPENSE;
                case 3 -> menu = CHECK_ACCOUNT;
                case 4 -> menu = ADD_CATEGORY;
                case 5 -> menu = INITIALIZATION;
                case 6 -> exit();
            }
            input = menu.callMenu();
        }

    }

    private void exit() {
        System.out.println("가계부를 종료합니다");
        System.exit(0);
    }

}
