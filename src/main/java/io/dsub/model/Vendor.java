package io.dsub.model;

import io.dsub.constants.DataType;

import java.io.Serializable;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * An immutable vendor model
 */
public class Vendor extends Model implements Serializable {
    private final Integer id;
    private final String name;
    private final Integer catId;
    private static final long serialVersionUID = 1L;

    public Vendor(String name) {
        this(null, name, null);
    }

    public Vendor(String name, Integer catId) {
        this(null, name, catId);
    }

    public Vendor(Integer id, String name, Integer catId) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vendor vendor = (Vendor) o;

        if (id != vendor.id) return false;
        if (catId != vendor.catId) return false;
        return name.equals(vendor.name);
    }
}
