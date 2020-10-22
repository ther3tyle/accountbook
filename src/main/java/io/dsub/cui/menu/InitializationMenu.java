package io.dsub.cui.menu;

import io.dsub.AppState;
import io.dsub.constants.UIString;
import io.dsub.model.Category;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;
import io.dsub.service.MockCategoryService;
import io.dsub.service.MockTransactionService;
import io.dsub.service.MockVendorService;
import io.dsub.service.ModelService;
import io.dsub.util.Initializer;

import javax.naming.InsufficientResourcesException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class InitializationMenu implements Menu {
    @Override
    public int call() {
        try {
            System.out.println(UIString.INIT_ATTEMPT);
            AppState.getInstance().getConn().close();
            Initializer.init();
            System.out.println(UIString.INIT_COMPLETE + "\n");
        } catch (FileNotFoundException | SQLException | InsufficientResourcesException e) {
            System.out.println("초기화 오류 발생: " + e.getMessage());
            System.out.println("앱을 종료합니다."); // TODO: impl retry
        }
        return backToMainMenu();
    }
}
