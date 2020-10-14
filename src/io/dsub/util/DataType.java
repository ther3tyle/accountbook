package io.dsub.util;

public enum DataType {
    TRANSACTION_FILE("trans_db.txt");

    DataType(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
