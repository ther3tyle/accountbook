package io.dsub.util;

public enum DataType {
    TRANSACTION("trans_db"), VENDOR("vend_db"), CATEGORY("cat_db");

    DataType(String fileName) {
        this.fileName = fileName;
    }

    private final String fileName;

    public String getFileName() {
        return fileName;
    }
}
