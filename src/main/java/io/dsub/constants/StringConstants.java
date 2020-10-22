package io.dsub.constants;

import java.io.File;

public class StringConstants {
    private StringConstants(){}
    public static final String CONN_STRING = "jdbc:h2:" + System.getProperty("user.dir") + File.separator + "db" + File.separator + "h2;MODE=MySQL";
    public static final String SCHEMA = "ACCOUNT";
    public static final String CATEGORY = "CATEGORY";
    public static final String VENDOR = "VENDOR";
    public static final String TRANSACTION = "TRANSACTION";
    public static final String INVALID_INPUT = "잘못된 입력입니다. ";
    public static final String RE_ENTER_PROMPT = "다시 입력해주세요. ";
    public static final String ASK_INPUT_CONFIRM = "입력하신 내용이 맞습니까? ";
    public static final String CANNOT_RECOGNIZE = "인식할 수 없습니다. ";

}
