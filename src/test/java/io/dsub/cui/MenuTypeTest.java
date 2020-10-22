package io.dsub.cui;

import io.dsub.constants.MenuType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuTypeTest {

    @Test
    void getTitle() {
        String result = MenuType.INCOME.getTitle();
        assertEquals("수입 입력",result);
    }
}