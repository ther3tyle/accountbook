package io.dsub;

public class AppStatus {

    private static AppStatus instance;

    private AppStatus() {}

    public static AppStatus getInstance() {
        if(instance == null) {

            instance = new AppStatus();
        }

        return instance;
    }


}
