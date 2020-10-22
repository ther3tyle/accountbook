package io.dsub.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputValidatorTest {

    @Test
    void isValidNumInput() {

        List<String> testTrue = new ArrayList<>();
        testTrue.add("2");

        for (String str: testTrue) {
            boolean result = InputValidator.isValidNumInput(str);
            assertTrue(result);
        }

    }

    @Test
    void isValidDateInput() {
        List<String> testTrue = new ArrayList<>();
        testTrue.add("2020-02-29"); // 29일이 있는 2월
        testTrue.add("2020-01-31"); // 31일이 있는 달

        List<String> testFalse = new ArrayList<>();
        testFalse.add("2020-02-31"); //2월이 29이상 입력
        testFalse.add("2019-02-29"); //29이 없는 2월
        testFalse.add("2020-02-55"); // 날짜가 31일 이상 입력
        testFalse.add("2020-04-31"); // 30일까지 있는달에 31일 입력
        testFalse.add("2020-14-01"); // 월이 12를 초과


        for (String str: testTrue) {
            boolean result = InputValidator.isValidDateInput(str);
            assertTrue(result);
        }

        for (String str: testFalse) {
            boolean result = InputValidator.isValidDateInput(str);
            assertFalse(result);
        }
    }

    @Test
    void isValidAmountInput() {
    }

    @Test
    void isValidVendorInput() {
    }

    @Test
    void isValidCategoryInput() {
    }

    @Test
    void testMatches() {
        assertFalse(InputValidator.matches("hello", "a", "b", "c"));
        assertTrue(InputValidator.matches("hello", "a", "hello", "c"));
        assertTrue(InputValidator.matches("hello", "a", "HelLo", "c"));
        assertFalse(InputValidator.matches("hello", "a HelLeo c"));
        assertFalse(InputValidator.matches("hello", "a HelLo1 c"));
        assertTrue(InputValidator.matches("hello", "a HelLo c"));
    }
}