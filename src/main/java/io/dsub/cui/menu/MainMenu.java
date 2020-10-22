package io.dsub.cui.menu;

import io.dsub.cui.MenuType;
import io.dsub.util.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainMenu implements Menu {

    private final List<MenuType> menuList = Arrays.asList(MenuType.values());

    @Override
    public int callMenu() {

        print();
        String input = checkInputValidation(getKeyboardInput());
        System.out.println(input);
        return Integer.parseInt(input);
    }

    private void print() {
        System.out.println("메인 메뉴");
        for (int i = 0; i < menuList.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), menuList.get(i).getTitle());
        }
    }

    private String getKeyboardInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("번호를 입력해주세요");
        return scanner.nextLine();
    }

    private String checkInputValidation(String input) {

        if (Validator.isValidNumInput(input) && Integer.parseInt(input) <= menuList.size()) {
            return input;
        } else {
            System.out.println("잘못된 입력입니다");
            return checkInputValidation(getKeyboardInput());
        }

    }


}
