package io.dsub.cui.menu;

import io.dsub.AppState;
import io.dsub.model.Category;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;
import io.dsub.service.MockCategoryService;
import io.dsub.service.MockTransactionService;
import io.dsub.service.MockVendorService;
import io.dsub.service.ModelService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class InitializationMenu implements Menu {

    private final ModelService<Vendor> VENDOR_MODEL_SERVICE = new MockVendorService();
    private final ModelService<Transaction> TRANSACTION_MODEL_SERVICE = new MockTransactionService();
    private final ModelService<Category> CATEGORY_MODEL_SERVICE = new MockCategoryService();

    @Override
    public int callMenu() {
        System.out.println("call Initialization");

        return backToMainMenu();
    }

    private void initialize() throws FileNotFoundException, SQLException {
        InputStream sqlInputStream = getClass().getClassLoader().getResourceAsStream("reset_schema.sql");
        if (sqlInputStream == null) {
            throw new FileNotFoundException("failed to retrieve reset_schema.sql");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(sqlInputStream));

        String sqlString = reader.lines()
                .reduce((acc, curr) ->  curr + "\n" + acc)
                .orElse("");
        if (sqlString.length() == 0) {
            throw new FileNotFoundException("reset_schema.sql found but is empty");
        }

        Connection conn = AppState.getInstance().getConn();
        conn.createStatement().execute(sqlString);
        System.out.println("초기화가 완료되었습니다");
    }

    private String getKeyboardInput() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();
    }

    private int checkInitialization() {

        return 0;
    }


}
