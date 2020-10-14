package io.dsub.util;

public enum LocalDataType {
    TRANSACTION("trans_db"), VENDOR("vend_db"), CATEGORY("cat_db");

    LocalDataType(String fileName) {
        this.fileName = fileName;
    }

    private final String fileName;

    public String getFileName() {
        return fileName;
    }
}
