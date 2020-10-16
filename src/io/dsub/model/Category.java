package io.dsub.model;

import io.dsub.util.DataType;

import java.io.Serializable;
import java.util.function.Function;

/**
 * An immutable category model
 */
public class Category extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String[] strings) {
        this(Integer.parseInt(strings[0]), strings[1]);
    }

    @Override
    public String getId() {
        return String.valueOf(this.id);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + "," + name;
    }

    public static DataType getDataType() {
        return DataType.CATEGORY;
    }

    public static Function<String, Model> getParser() {
        return (String s) -> {
            String[] strings = s.split(",");
            if (strings.length < 2) return null;
            try {
                int id = Integer.parseInt(strings[0]);
                return new Category(id, strings[1]);
            } catch (NumberFormatException e) {
                return null;
            }
        };
    }
}
