package io.dsub.cui;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class MenuHandler {

    List<MenuType> menuList() {

        MenuType[] menus = MenuType.values();
        List<MenuType> list = Arrays.asList(menus);
        return list;
    }

    void callMainMenu() {

        printMenu(menuList());
        executeMenu(checkMenuNum(menuList()));

    }

    public void executeMenu(int checkMenuNum) {
        MenuType selectedMenu = menuList().get(checkMenuNum-1);
        MenuMethods.selectMenu(selectedMenu);
        callMainMenu();
    }

    public int checkMenuNum(List<MenuType> list) { //기능 분리 필요
        int input = 0;
        outer:
        while (true) {
            System.out.println("");
            System.out.println("이용하실 메뉴의 번호를 입력해 주세요");
            input = getKeyboardInput();
            for (int i = 0; i < list.size(); i++) {
                if (input > 0 && input <= list.size()) {
                    break outer;
                }
            }
            System.out.println("번호를 잘못 입력하셨습니다");
        }
        return input;
    }


     int getKeyboardInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return Character.getNumericValue(input.charAt(0)); // String or Character 입력시 에러가 안나게
    }


    private void printMenu(List<MenuType> list) {
        System.out.println("메인 메뉴");
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d. %s\n",(i + 1), list.get(i).getTitle());
        }
    }


}

class Test {
    public static void main(String[] args) {

        MenuHandler menuHandler = new MenuHandler();

        menuHandler.callMainMenu();

    }
}
