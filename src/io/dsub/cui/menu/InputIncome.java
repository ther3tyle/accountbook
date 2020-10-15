package io.dsub.cui.menu;

public class InputIncome implements Menu {
    int number = 1;
    String title = "수입 입력";

    @Override
    public void execute() {
        System.out.println("1번 메소드");
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }
}
