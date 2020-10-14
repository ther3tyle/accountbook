package io.dsub.cui.menu;


public class InputExpense implements Menu {

    int number = 2;
    String title = "지출 입력";

    @Override
    public void execute() {
        System.out.println("2번 메소드");
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }
}
