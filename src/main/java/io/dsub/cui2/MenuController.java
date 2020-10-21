package io.dsub.cui2;

import io.dsub.cui2.menu.*;

import java.util.Scanner;

public class MenuController {

    Menu menu;

    public void startAccountBook() {
        selectMenu();
    }

    public void selectMenu() {

        String input = "";
        while (true){
            switch (input) {
                case "1", "2" -> menu = new InputTransaction();
                case "3" -> menu = new Check();
                case "4" -> menu = new AddCategory();
                case "5" -> menu = new Initialization();
                case "6" -> exit();
                default -> menu = new Main();
            }

            String output = menu.callMenu();
            input = output;
        }


    }

    private String getKeyboardInput() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();
    }

    private void exit() {
        System.out.println("가계부를 종료합니다");
        System.exit(0);
    }

}
