package io.dsub.constants;

public enum DataType {
    TRANSACTION("trans_db", "TRANSACTION"),
    VENDOR("vend_db", "VENDOR"),
    CATEGORY("cat_db", "CATEGORY");

    DataType(String fileName, String tableName) {
        this.fileName = fileName;
        this.tableName = tableName;
    }

    private final String fileName;
    private final String tableName;

    public String getFileName() {
        return fileName;
    }

    public String getTableName() {
        return tableName;
    }
}
