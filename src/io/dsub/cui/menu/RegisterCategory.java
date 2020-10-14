package io.dsub.cui.menu;

public class RegisterCategory implements Menu {
    int number = 4;
    String title = "카테고리 등록";

    @Override
    public void execute() {
        System.out.println("4번 메소드");
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }
}
