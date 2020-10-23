package io.dsub.cui.menu;

import io.dsub.constants.MenuType;
import io.dsub.constants.UIString;
import io.dsub.util.InputValidator;

import java.util.Arrays;
import java.util.List;

public class MainMenu implements Menu {

    private final List<MenuType> menuList = Arrays.asList(MenuType.values());

    @Override
    public int call() {
        printMenuOpts();
        String input = checkInputValidation(inputHandler.take());
        return Integer.parseInt(input);
    }

    private void printMenuOpts() {
        StringBuilder builder = new StringBuilder();
        builder.append("메인 메뉴\n");
        for (int i = 0; i < menuList.size(); i++) {
            builder.append(String.format("%d. %s\n", (i + 1), menuList.get(i).getTitle()));
        }
        builder.append("\n");
        builder.append("번호를 입력해주세요.");
        System.out.println(builder.toString());
    }

    private String checkInputValidation(String input) {
        if (InputValidator.matches(input, UIString.EXITS)) {
            return "6";
        }
        if (InputValidator.isValidNumInput(input) && Integer.parseInt(input) <= menuList.size()) {
            return input;
        } else {
            System.out.println("잘못된 입력입니다");
            printMenuOpts();
            return checkInputValidation(inputHandler.take());
        }
    }
}
