package io.dsub.util;

public class Validator {
    private Validator(){}

    public static boolean isValidNumInput(String in) {
        return in.matches("^[0-9]+$");
    }
}
