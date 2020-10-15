package io.dsub.cui.menu;

public class CheckAccount implements Menu {
    int number = 3;
    String title = "조회";

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public void execute() {
        System.out.println("3번 메소드");
    }
}
