package io.dsub.util;

public class Validator {
    private Validator(){}

    public static boolean isValidNumInput(String in) {
        return in.matches("^[0-9]+$");
    }

    public static boolean isValidDateInput(String in) {
        return in.matches("^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$");
    }

    public static boolean isValidAmountInput(String in) {
        return in.matches("^\\d{1,18}$"); //Long 범위가 19자리수, 18자리까지만 입력받기
    }

    public static boolean isValidVendorInput(String in) {
        return in.matches("^[a-zA-z가-힣]{1,20}");
    }

    public static boolean isValidCategoryInput(String in) {
        return in.matches("^[a-zA-z가-힣]{1,20}");
    }
}
