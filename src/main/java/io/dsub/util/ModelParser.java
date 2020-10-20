package io.dsub.util;

import io.dsub.constants.DataType;
import io.dsub.model.Category;
import io.dsub.model.Model;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;

import java.util.NoSuchElementException;
import java.util.function.Function;

public class ModelParser {
    public static Function<String, Model> get(DataType type) {
        Function<String, Model> parser;
        switch (type) {
            case VENDOR -> parser = Vendor.getParser();
            case CATEGORY -> parser = Category.getParser();
            case TRANSACTION -> parser = Transaction.getParser();
            default -> throw new NoSuchElementException("type of " + type.getClass().getName() + " is not supported");
        }
        return parser;
    }
}
