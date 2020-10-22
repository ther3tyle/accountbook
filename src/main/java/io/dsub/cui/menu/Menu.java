package io.dsub.cui.menu;

// TODO: consider refactoring all child classes to be singleton
public interface Menu {

    int callMenu();

    default int backToMainMenu() {
        System.out.println("메인 메뉴로 돌아갑니다");
        return 0;
    }

}
