package io.dsub.constants;

import java.io.File;

public class StringConstants {
    private StringConstants(){}
    public static final String CONN_STRING = "jdbc:h2:" + System.getProperty("user.dir") + File.separator + "db" + File.separator + "h2;MODE=MySQL";
    public static final String SCHEMA = "ACCOUNT";
    public static final String CATEGORY = "CATEGORY";
    public static final String VENDOR = "VENDOR";
    public static final String TRANSACTION = "TRANSACTION";
}