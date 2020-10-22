package io.dsub.cui;

import io.dsub.cui.menu.*;

public class MenuController {

    private final Menu ADD_CATEGORY = new AddCategory();
    private final Menu CHECK_ACCOUNT = new CheckAccount();
    private final Menu INITIALIZATION = new Initialization();
    private final Menu INPUT_INCOME = new InputTransaction(MenuType.INCOME);
    private final Menu INPUT_EXPENSE = new InputTransaction(MenuType.EXPENSE);
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
