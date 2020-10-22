package io.dsub.cui.menu;

import java.sql.SQLException;

// TODO: consider refactoring all child classes to be singleton
public interface Menu {

    int callMenu() throws SQLException;

    default int backToMainMenu() {
        System.out.println("메인 메뉴로 돌아갑니다");
        return 0;
    }

}
