package io.dsub.cui;

import io.dsub.cui.menu.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MenuHandler {

    List<Menu> sampleMenu() {

        List<Menu> list = new ArrayList<>();

        list.add(MenuFactory.getMenu("수입"));
        list.add(MenuFactory.getMenu("지출"));
        list.add(MenuFactory.getMenu("조회"));
        list.add(MenuFactory.getMenu("카테고리"));
        list.add(MenuFactory.getMenu("초기화"));
        return list;
    }

    void callMainMenu() {

        printMenu(sampleMenu());
        executeMenu(checkMenuNum(sampleMenu()));


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
