package io.dsub.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorTest {

    @Test
    void isValidNumInput() {

        List<String> testTrue = new ArrayList<>();
        testTrue.add("2");

        for (String str: testTrue) {
            boolean result = Validator.isValidNumInput(str);
            assertTrue(result);
        }

    }

    @Test
    void isValidDateInput() {
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
}