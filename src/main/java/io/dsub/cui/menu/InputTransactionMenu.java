package io.dsub.cui.menu;

import io.dsub.Application;
import io.dsub.constants.MenuType;
import io.dsub.model.Category;
import io.dsub.model.Model;
import io.dsub.model.Transaction;
import io.dsub.model.Vendor;
import io.dsub.service.*;
import io.dsub.util.InputValidator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class InputTransactionMenu implements Menu {

    private List<String> inputParams;
    public MenuType menuType;
    private ModelService<Vendor> vendorService;
    private ModelService<Transaction> transactionService;
    private ModelService<Category> categoryService;

    private List<Vendor> vendorList;
    private List<Category> categoryList;

    // should be called when we need to review
    // private final List<Category> catList = CATEGORY_MODEL_SERVICE.findAll();

    public InputTransactionMenu(MenuType menuType) {
        this.menuType = menuType;
    }

    @Override
    public int call() {
        vendorService = new VendorServiceImpl();
        transactionService = new TransactionServiceImpl();
        categoryService = new CategoryServiceImpl();
        return operateTransaction(inputStatementMenu());
    }

    private int operateTransaction(List<String> inputList) {
        if (inputList != null) {
            long amount = Long.parseLong(inputList.get(inputParams.indexOf("Amount")));
            String vendorName = inputList.get(inputParams.indexOf("Vendor"));
            String categoryName = inputList.get(inputParams.indexOf("Category"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(inputList.get(inputParams.indexOf("Date")), formatter);

            try {
                Category category = new Category(categoryName);
                int catId = Integer.parseInt(categoryService.save(category));

                Vendor vendor = new Vendor(vendorName, catId);
                int vendorId = Integer.parseInt(vendorService.save(vendor));

                Transaction transaction = new Transaction(amount, vendorId);
                transactionService.save(transaction);

                System.out.println();
                Application.wait(1.5);


//                System.out.println(transaction.getId()); we don't need it for now
            } catch (SQLException e) {
                Logger.getLogger(getClass().getName()).severe(e.getMessage());
            }

        }
        return backToMainMenu();

    }


    private List<String> inputStatementMenu() {
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
            String input = inputHandler.take();

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
            return InputValidator.isValidDateInput(input);
        } else if (list.size() == inputParams.indexOf("Amount")) {
            return InputValidator.isValidAmountInput(input);
        } else if (list.size() == inputParams.indexOf("Vendor")) {
            return InputValidator.isValidVendorInput(input);
        } else if (list.size() == inputParams.indexOf("Category")) {
            if (input.matches("^[0-9]+$")) {
                int val = Integer.parseInt(input);
                return val > 0 && val < categoryList.size();
            }
            return false;
        }
        return false;
    }


    private void printStatementMenuInfo(List<String> list) {
        if (list.size() != 0) {
            System.out.println("입력값 확인");
            System.out.println(list.toString());
            System.out.println();
        }

        if (list.size() == inputParams.indexOf("Date")) {
            // TODO: make better parser
            System.out.println("거래 날짜를 입력하세요. 예) 2020-04-24 (현재날짜: C 메인메뉴: Q)");
        } else if (list.size() == inputParams.indexOf("Amount")) {
            System.out.println("금액을 입력하세요. 예) 3500 (이전단계: P 메인메뉴: Q)");
        } else if (list.size() == inputParams.indexOf("Vendor")) {
            vendorList = vendorService.findAll();
            printItems(vendorList);
            System.out.println("사용처를 입력하세요 (이전단계: P 메인메뉴: Q)");
        } else if (list.size() == inputParams.indexOf("Category")) {
            categoryList = categoryService.findAll();
            printItems(categoryList);
            System.out.println("카테고리 번호를 입력하세요 (이전단계: P 메인메뉴: Q)");
        }
    }

    private void printItems(List<? extends Model> items) {
        if (items == null || items.isEmpty()) return;
        Model m = items.get(0);
        List<String> names;
        if (m instanceof Category) {
            names = items.stream()
                    .map(item -> (Category) item)
                    .map(Category::getName)
                    .collect(Collectors.toList());
        } else if (m instanceof Vendor) {
            names = items.stream()
                    .map(item -> (Vendor) item)
                    .map(Vendor::getName)
                    .collect(Collectors.toList());
        } else {
            return;
        }

        for (int i = 0; i < names.size(); i++) {
            int idx = i + 1;
            String val = names.get(i);
            if (i > 0 && i % 3 == 0) {
                writer.newLine();
            }
            writer.write(String.format("%d. %s\t\t", idx, val));
        }
        writer.newLine();
        writer.newLine();
        writer.flush();
    }
}
