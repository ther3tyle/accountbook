package io.dsub.cui.menu;

/**
 * Menu interface for menu interaction items
 */
public interface Menu {
    void execute();

    default void returnMainMenu() {
        System.out.println("메인메뉴로돌아갑니다");
    }

    int getNumber();

    String getTitle();
}
