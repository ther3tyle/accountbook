package io.dsub.cui.menu;

import io.dsub.cui.MenuType;
import io.dsub.model.Category;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;
import io.dsub.service.MockCategoryService;
import io.dsub.service.MockTransactionService;
import io.dsub.service.MockVendorService;
import io.dsub.service.ModelService;
import io.dsub.util.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputTransactionMenu implements Menu {

    private List<String> inputParams;
    public MenuType menuType;
    private final ModelService<Vendor> VENDOR_MODEL_SERVICE = new MockVendorService();
    private final ModelService<Transaction> TRANSACTION_MODEL_SERVICE = new MockTransactionService();
    private final ModelService<Category> CATEGORY_MODEL_SERVICE = new MockCategoryService();

    public InputTransactionMenu(MenuType menuType) {
        this.menuType = menuType;
    }

    @Override
    public int callMenu() {

        return operateTransaction(inputStatementMenu());
    }

    private int operateTransaction(List<String> inputList) {

        if (inputList != null) {
            Long amount = Long.parseLong(inputList.get(inputParams.indexOf("Amount")));
            String vendor = inputList.get(inputParams.indexOf("Vendor"));
            String category = inputList.get(inputParams.indexOf("Category"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(inputList.get(inputParams.indexOf("Date")), formatter);

            System.out.println(amount + "," + localDate + "," + vendor + "," + category);

        }
        return backToMainMenu();

    }


    private List<String> inputStatementMenu() {
        Scanner scanner = new Scanner(System.in);
        List<String> list = new ArrayList<>();
        inputParams = Arrays.asList("Date", "Amount", "Vendor", "Category");

        if (menuType == MenuType.INCOME) {
            System.out.println("수입 입력");
        } else {
            System.out.println("지출 입력");
        }

        boolean isInputFinished = false;
        while (!isInputFinished) {


            printStatementMenuInfo(list);
            String input = scanner.nextLine();

            // 예외처리
            if (input.equals("p") || input.equals("P")) {
                if (list.size() != 0) {
                    list.remove(list.size() - 1);
                }
                System.out.println("이전 단게로");
                continue;

            } else if (input.equals("q") || input.equals("Q")) {
                return null;
            }

            // 본기능
            if (!isInputStatementValid(list, input)) {
                System.out.println("잘못된 입력입니다");
                continue;
            } else {
                input = inputHelper(list, input); //이름고민
                list.add(input);
            }
            isInputFinished = list.size() >= inputParams.size();
        }

        return checkInputStatement(list);
    }


    private String inputHelper(List<String> list, String input) {

        if (list.size() == inputParams.indexOf("Date") && input.equalsIgnoreCase("c")) {
            LocalDate localDatetime = LocalDate.now();
            return localDatetime.toString();
        } else if (list.size() == inputParams.indexOf("Amount") && menuType == MenuType.EXPENSE) {
            String minus = "-";
            return minus + input;
        } else {
            return input;
        }

    }


    private List<String> checkInputStatement(List<String> list) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(list.toString());
        System.out.println("입력을 확인해주세요?(Y/N)");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("y")) {
            System.out.println("입력이 완료되었습니다");
            return list;
        } else if (input.equalsIgnoreCase("n")) {
            System.out.println("다시 입력해주세요");
            return inputStatementMenu();
        } else {
            System.out.println("잘못된 입력입니다");
            return checkInputStatement(list);
        }
    }

    private boolean isInputStatementValid(List<String> list, String input) {


        if (list.size() == inputParams.indexOf("Date")) {
            return Validator.isValidDateInput(input);
        } else if (list.size() == inputParams.indexOf("Amount")) {
            return Validator.isValidAmountInput(input);
        } else if (list.size() == inputParams.indexOf("Vendor")) {
            return Validator.isValidVendorInput(input);
        } else if (list.size() == inputParams.indexOf("Category")) {
            return Validator.isValidCategoryInput(input);
        }
        return false;
    }


    private void printStatementMenuInfo(List<String> list) {


        if (list.size() != 0) {
            System.out.println("입력값 확인");
            System.out.println(list.toString());
        }

        if (list.size() == inputParams.indexOf("Date")) {
            System.out.println("거래 날짜를 입력하세요 (현재날짜: C 메인메뉴: Q)");
        } else if (list.size() == inputParams.indexOf("Amount")) {
            System.out.println("금액을 입력하세요(이전단계: P 메인메뉴: Q)");
        } else if (list.size() == inputParams.indexOf("Vendor")) {
            System.out.println("사용처를 입력하세요(이전단계: P 메인메뉴: Q)");
        } else if (list.size() == inputParams.indexOf("Category")) {
            System.out.println("카테고리를 입력하세요(이전단계: P 메인메뉴: Q)");
        }
    }



}
