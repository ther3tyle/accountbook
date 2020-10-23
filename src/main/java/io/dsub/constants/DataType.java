package io.dsub.constants;

public enum DataType {
    TRANSACTION("trans_db", "TRANSACTION"),
    VENDOR("vend_db", "VENDOR"),
    CATEGORY("cat_db", "CATEGORY");

    private final String fileName;
    private final String tableName;
    DataType(String fileName, String tableName) {
        this.fileName = fileName;
        this.tableName = tableName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTableName() {
        return tableName;
    }
}
