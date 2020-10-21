package io.dsub.cui;

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

public class MenuController {


    private static List<String> inputParams;
    private static final ModelService<Vendor> VENDOR_MODEL_SERVICE = new MockVendorService();
    private static final ModelService<Transaction> TRANSACTION_MODEL_SERVICE = new MockTransactionService();
    private static final ModelService<Category> CATEGORY_MODEL_SERVICE = new MockCategoryService();

    public static void selectMenu(MenuType menu) {

        switch (menu) {
            case INCOME, EXPENSE -> operateTransaction(inputStatementMenu(menu));
            case CHECK -> checkAccount();
            case CATEGORY -> addCategory();
            case INITIALIZATION -> initialize();
            case EXIT -> exitAccountBook();
        }

    }


    public static void operateTransaction(List<String> inputList) {

        if (inputList != null) {
            Long amount = Long.parseLong(inputList.get(inputParams.indexOf("Amount")));
            String vendor = inputList.get(inputParams.indexOf("Vendor"));
            String category = inputList.get(inputParams.indexOf("Category"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(inputList.get(inputParams.indexOf("Date")), formatter);

            System.out.println(amount + "," + localDate + "," + vendor + "," + category);

        } else {
            System.out.println("callMainMenu");
        }

    }


    public static List<String> inputStatementMenu(MenuType menu) {
        Scanner scanner = new Scanner(System.in);
        List<String> list = new ArrayList<>();
        inputParams = Arrays.asList("Date", "Amount", "Vendor", "Category");

        if (menu == MenuType.INCOME) {
            System.out.println("수입 입력");
        } else {
            System.out.println("지출 입력");
        }

        boolean isInputFinished = false;
        while (!isInputFinished) {


            printStatementMenuInfo(list);
            String input = scanner.nextLine();

            if (input.equals("p") || input.equals("P")) {
                if (list.size() != 0) {
                    list.remove(list.size() - 1);
                }
                System.out.println("이전 단게로");
                continue;

            } else if (input.equals("q") || input.equals("Q")) {
                return null;
            }

            if (!isInputStatementValid(list, input)) {
                System.out.println("잘못된 입력입니다");
                continue;
            } else {
                input = inputHelper(list, menu, input); //이름고민
                list.add(input);
            }
            isInputFinished = list.size() >= inputParams.size();
        }

        return checkInputStatement(list, menu);
    }


    private static String inputHelper(List<String> list, MenuType menu, String input) {

        if (list.size() == inputParams.indexOf("Date") && input.equalsIgnoreCase("c")) {
            LocalDate localDatetime = LocalDate.now();
            return localDatetime.toString();
        } else if (list.size() == inputParams.indexOf("Amount") && menu == MenuType.EXPENSE) {
            String minus = "-";
            return minus + input;
        } else {
            return input;
        }

    }


    public static void checkAccount() {
        System.out.println("조회");
    }

    public static void addCategory() {
        System.out.println("카테고리 추가");
    }

    public static void initialize() {
        System.out.println("가계부 초기화");
    }

    public static void exitAccountBook() {
        System.out.println("가계부가 종료 되었습니다");
        System.exit(0);
    }

    private static List<String> checkInputStatement(List<String> list, MenuType menu) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(list.toString());
        System.out.println("입력을 확인해주세요?(Y/N)");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("y")) {
            System.out.println("입력이 완료되었습니다");
            return list;
        } else if (input.equalsIgnoreCase("n")) {
            System.out.println("다시 입력해주세요");
            return inputStatementMenu(menu);
        } else {
            System.out.println("잘못된 입력입니다");
            return checkInputStatement(list, menu);
        }
    }

    private static boolean isInputStatementValid(List<String> list, String input) {

//        return switch (list.size()) {
//            case 1 -> Validator.isValidDateInput(input);
//            case 2 -> Validator.isValidAmountInput(input);
//            case 3 -> Validator.isValidVendorInput(input);
//            case 4 -> Validator.isValidCategoryInput(input);
//            default -> false;
//        };

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


    private static void printStatementMenuInfo(List<String> list) {


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
