package io.dsub.cui;

import io.dsub.cui.menu.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MenuHandler {

    List<Menu> sampleMenu(String... keywords) {

        List<Menu> list = new ArrayList<>();

        for (String keyword : keywords) {
            list.add(MenuFactory.getMenu(keyword));
        }

        return list;
    }

    void callMainMenu() {
        List<Menu> menuList = sampleMenu("수입", "지출", "조회", "카테고리", "초기화");
        printMenu(menuList);
        executeMenu(checkMenuNum(menuList));
    }

    public void executeMenu(int checkMenuNum) {
        for (Menu menu : sampleMenu()) {
            if (menu.getNumber() == checkMenuNum) {
                menu.execute();
                break;
            }
        }
    }

    public int checkMenuNum(List<Menu> list) {
        int input = 0;
        outer:
        while (true) {
            System.out.println("");
            System.out.println("이용하실 메뉴의 번호를 입력해 주세요");
            input = Integer.parseInt(getKeyboardInput());
            for (Menu menu : list) {
                if (menu.getNumber() == input) {
                    break outer;
                }
            }
            System.out.println("번호를 잘못 입력하셨습니다");
        }
        return input;
    }


    private String getKeyboardInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }


    private void printMenu(List<Menu> list) {
        for (Menu menu : list) {
            System.out.printf("%d. %s\n", menu.getNumber(), menu.getTitle());
        }
    }


}

class Test {
    public static void main(String[] args) {

        MenuHandler menuHandler = new MenuHandler();

        menuHandler.callMainMenu();

    }
}
