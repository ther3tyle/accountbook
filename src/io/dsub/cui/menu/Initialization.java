package io.dsub.cui.menu;

public class Initialization implements Menu {
    int number = 5;
    String title = "초기화";

    @Override
    public void execute() {
        System.out.println("5번 메소드");
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }
}
