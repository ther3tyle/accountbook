package io.dsub.cui.menu;

import io.dsub.model.Category;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;
import io.dsub.service.MockCategoryService;
import io.dsub.service.MockTransactionService;
import io.dsub.service.MockVendorService;
import io.dsub.service.ModelService;

import java.util.Scanner;

public class InitializationMenu implements Menu {

    private final ModelService<Vendor> VENDOR_MODEL_SERVICE = new MockVendorService();
    private final ModelService<Transaction> TRANSACTION_MODEL_SERVICE = new MockTransactionService();
    private final ModelService<Category> CATEGORY_MODEL_SERVICE = new MockCategoryService();

    @Override
    public int callMenu() {
        System.out.println("call Initialization");
        return 0;
    }

    private void initialize() {
//        TRANSACTION_MODEL_SERVICE.deleteAll();
//        VENDOR_MODEL_SERVICE.deleteAll();
//        CATEGORY_MODEL_SERVICE.deleteAll();
    }

    private String getKeyboardInput() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();
    }

    private int checkInitialization() {

        return 0;
    }


}
