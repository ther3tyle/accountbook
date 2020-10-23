package io.dsub.model;

import io.dsub.constants.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {
    Category category;

    @BeforeEach
    void prep() {
        category = new Category(3, "오징어");
    }

    @Test
    void getId() {
        assertEquals(3, Integer.parseInt(category.getId()));
    }

    @Test
    void getName() {
        assertEquals("오징어", category.getName());
    }

    @Test
    void testToString() {
        assertEquals("3,오징어", category.toString());
    }

    @Test
    void getDataType() {
        assertEquals(DataType.CATEGORY, Category.getDataType());
    }

    @Test
    void getParser() {
        Function<String, Model> parser = Category.getParser();
        assertDoesNotThrow(() -> {
            Category tempCat = (Category) parser.apply("57346,갑분싸");
            assertEquals(57346, Integer.parseInt(tempCat.getId()));
            assertEquals("갑분싸", tempCat.getName());
        });
    }
}