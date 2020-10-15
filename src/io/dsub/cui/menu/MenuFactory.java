package io.dsub.cui.menu;

public class MenuFactory {
    public static Menu getMenu(String menuType) {
        return switch (menuType) {
            case "수입" -> new InputIncome();
            case "지출" -> new InputExpense();
            case "조회" -> new CheckAccount();
            case "카테고리" -> new RegisterCategory();
            case "초기화" -> new Initialization();
            default -> null;
        };
    }
}
