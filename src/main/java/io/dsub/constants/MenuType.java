package io.dsub.constants;

public enum MenuType {

    INCOME("수입 입력"),
    EXPENSE("지출 입력"),
    CHECK("조회"),
    CATEGORY("카테고리 추가"),
    INITIALIZATION("가계부 초기화"),
    EXIT("가계부 종료");

    private final String title;


    MenuType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


}
