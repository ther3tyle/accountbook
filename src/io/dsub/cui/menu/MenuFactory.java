package io.dsub.cui.menu;

public class MenuFactory {
    public static Menu getMenu(String menuType) {
        Menu menu = null;

        if (menuType.equals("수입")) {
            menu = new InputIncome();
        } else if (menuType.equals("지출")) {
            menu = new InputExpense();
        } else if (menuType.equals("조회")) {
            menu = new CheckAccount();
        } else if (menuType.equals("카테고리")) {
            menu = new RegisterCategory();
        } else if (menuType.equals("초기화")) {
            menu = new Initialization();
        }

        return menu;
    }
}
