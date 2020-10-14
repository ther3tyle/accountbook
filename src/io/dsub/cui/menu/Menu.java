package io.dsub.cui.menu;

//메뉴 추상클래스
public interface Menu {

    abstract void execute();

    default void returnMainMenu() {
        System.out.println("메인메뉴로돌아갑니다");
    }

    public int getNumber();

    public String getTitle();


}
