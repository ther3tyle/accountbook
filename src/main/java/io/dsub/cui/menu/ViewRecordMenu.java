package io.dsub.cui.menu;

import io.dsub.constants.UIString;
import io.dsub.model.Category;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;
import io.dsub.service.*;
import io.dsub.util.InputValidator;

import java.util.Arrays;
import java.util.List;

public class ViewRecordMenu implements Menu {

    @Override
    public int call() {
        TransactionService transactionService = new TransactionServiceImpl();
        List<Transaction> transactions = transactionService.findAll();
        transactions.forEach(this::printTransaction);
        return backToMainMenu();
    }

    private void printTransaction(Transaction item) {
        VendorService vendorService = new VendorServiceImpl();
        CategoryService categoryService = new CategoryServiceImpl();
        writer.write("날짜: " + item.getDate().toString() + "\t");
        writer.write("금액: " + Math.abs(item.getAmount()) + "\t");
        writer.write(item.getAmount() < 0 ? " 지출" : " 수입");
        Vendor v =  vendorService.findById(String.valueOf(item.getVendorId()));
        writer.write("사용처: " + v.getName() + "\t");
        Category c = categoryService.findById(v.getId());
        writer.write("카테고리: " + c.getName() + "\t");
        writer.newLine();
        writer.newLine();
        writer.flush();
    }

    private void printMenu() {

    }

    private String getKeyboardInput(){

        return null;
    }

    private void calcStatics() {

    }



}