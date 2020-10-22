package io.dsub.cui.menu;

import io.dsub.model.Category;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;
import io.dsub.service.MockCategoryService;
import io.dsub.service.MockTransactionService;
import io.dsub.service.MockVendorService;
import io.dsub.service.ModelService;

import java.util.Arrays;
import java.util.List;

public class CheckAccountMenu implements Menu {

    private final List<String> checkMenuList = Arrays.asList("day", "month", "year", "");
    private final ModelService<Vendor> VENDOR_MODEL_SERVICE = new MockVendorService();
    private final ModelService<Transaction> TRANSACTION_MODEL_SERVICE = new MockTransactionService();
    private final ModelService<Category> CATEGORY_MODEL_SERVICE = new MockCategoryService();

    @Override
    public int callMenu() {
        System.out.println("call check Account");

        return backToMainMenu();
    }

    private void printMenu() {

    }

    private String getKeyboardInput(){

        return null;
    }

    private void calcStatics() {

    }



}