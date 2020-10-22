package io.dsub.cui.menu;

import io.dsub.model.Category;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;
import io.dsub.service.MockCategoryService;
import io.dsub.service.MockTransactionService;
import io.dsub.service.MockVendorService;
import io.dsub.service.ModelService;

public class CheckAccount implements Menu {

    private final ModelService<Vendor> VENDOR_MODEL_SERVICE = new MockVendorService();
    private final ModelService<Transaction> TRANSACTION_MODEL_SERVICE = new MockTransactionService();
    private final ModelService<Category> CATEGORY_MODEL_SERVICE = new MockCategoryService();

    @Override
    public int callMenu() {
        System.out.println("call check Account");
        return 0;
    }



}