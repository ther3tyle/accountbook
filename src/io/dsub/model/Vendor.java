package io.dsub.model;

/**
 * An immutable vendor model
 */
public class Vendor {
    private final int id;
    private final String name;
    private final int catId;

    public Vendor(int id, String name, int catId) {
        this.id = id;
        this.name = name;
        this.catId = catId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCatId() {
        return catId;
    }
}
