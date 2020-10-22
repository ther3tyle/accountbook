package io.dsub.cui.menu;

import io.dsub.model.Category;
import io.dsub.service.CategoryServiceImpl;
import io.dsub.service.ModelService;
import io.dsub.util.Validator;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AddCategoryMenu implements Menu {

    private final ModelService<Category> CATEGORY_MODEL_SERVICE;
    private final List<Category> catList = null;

    public AddCategoryMenu() {
        CATEGORY_MODEL_SERVICE = new CategoryServiceImpl();
    }

    @Override
    public int callMenu() {

        System.out.println("카데고리 추가");
        printCategoryList();

        try {
            addCategory();
        } catch (SQLException e) {
            System.out.println("SQL ERROR");
            System.exit(1);
        }

        return backToMainMenu();
    }

    private void printCategoryList() {
        System.out.println("카테고리 목록");
        for (int i = 0; i < catList.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), catList.get(i));
        }
    }

    private void addCategory() throws SQLException {
        System.out.println("추가하실 카테고리 명을 입력하세요");
        String input = checkValidation(getKeyboardInput());

        boolean isCategoryCorrect = false;
        while (!isCategoryCorrect) {
            System.out.println("입력하신 내용이 맞습니까? (Y,N)");
            String input2 = getKeyboardInput();
            if (input2.equalsIgnoreCase("y")) {
                String result = CATEGORY_MODEL_SERVICE.save(new Category(input));
                System.out.println(result);
                printCategoryList();
                isCategoryCorrect = true;
            } else if (input2.equalsIgnoreCase("n")) {
                addCategory();
            } else {
                System.out.println("잘못된 입력입니다");
                isCategoryCorrect = false;
            }
        }


    }

    private String getKeyboardInput() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();
    }

    private String checkValidation(String input) {
        if (Validator.isValidCategoryInput(input)) {
            return input;
        } else {
            System.out.println("잘못된 입력입니다");
            return checkValidation(getKeyboardInput());
        }
    }


}
