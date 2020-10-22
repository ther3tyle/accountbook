package io.dsub.cui;

import io.dsub.constants.UIString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputHandler {
    private InputHandler() {
    }

    private static final InputHandler instance = new InputHandler();

    public static InputHandler getInstance() {
        return instance;
    }

    public String take() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print(">> ");
                String in = reader.readLine().trim();
                System.out.println();

                if (in.length() == 0) {
                    System.out.println(UIString.INVALID_INPUT + UIString.RE_ENTER_PROMPT);
                    continue;
                }

                return in;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
