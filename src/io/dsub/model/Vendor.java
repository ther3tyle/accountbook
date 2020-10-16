package io.dsub.model;

import io.dsub.util.DataType;

import java.util.function.Function;
import java.util.logging.Logger;

/**
 * An immutable vendor model
 */
public class Vendor extends Model {
    private final int id;
    private final String name;
    private final int catId;

    public Vendor(int id, String name, int catId) {
        this.id = id;
        this.name = name;
        this.catId = catId;
    }

    @Override
    public String getId() {
        return String.valueOf(this.id);
    }

    public String getName() {
        return name;
    }

    public int getCatId() {
        return catId;
    }

    public static DataType getDataType() {
        return DataType.VENDOR;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s", id, name, catId);
    }

    public static Function<String, Model> getParser() {
        return s -> {
            String[] strings = s.split(",");
            if (strings.length < 3) return null;
            try {
                int id = Integer.parseInt(strings[0]);
                String name = strings[1];
                int catId = Integer.parseInt(strings[2]);
                return new Vendor(id, name, catId);
            } catch (NumberFormatException e) {
                Logger logger = Logger.getLogger(Vendor.class.getName());
                logger.severe(e.getMessage());
                return null;
            }
        };
    }
}
