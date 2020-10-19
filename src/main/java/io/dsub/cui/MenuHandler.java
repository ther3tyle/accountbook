package io.dsub.cui;

import io.dsub.util.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class MenuHandler {

    List<MenuType> menuList() {
        MenuType[] menus = MenuType.values();
        return Arrays.asList(menus);
    }

    void callMainMenu() {
        printMenu(menuList());
        executeMenu(checkMenuNum(menuList()));
    }

    public void executeMenu(int checkMenuNum) {
        MenuType selectedMenu = menuList().get(checkMenuNum - 1);
        MenuMethods.selectMenu(selectedMenu);
        callMainMenu();
    }

    public int checkMenuNum(List<MenuType> list) {
        return getKeyboardInput("\n이용하실 메뉴의 번호를 입력해 주세요", (Integer n) -> n < list.size());
    }


    int getKeyboardInput(String promptMsg, Function<Integer, Boolean> validator) {
        System.out.println(promptMsg);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        boolean valid = Validator.isValidNumInput(input) && validator.apply(Integer.parseInt(input));

        while (!valid) {
            System.out.println("잘못된 입력입니다.");
            System.out.println(promptMsg);
            input = scanner.nextLine();
            valid = Validator.isValidNumInput(input); // && validator.apply(Integer.parseInt(input)) 안한이유?
        }

        return Integer.parseInt(input); // String or Character 입력시 에러가 안나게
    }


    private void printMenu(List<MenuType> list) {
        System.out.println("메인 메뉴");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), list.get(i).getTitle());
        }
    }

}
