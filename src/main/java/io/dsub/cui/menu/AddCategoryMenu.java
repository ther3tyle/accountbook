package io.dsub.cui.menu;

import io.dsub.model.Category;
import io.dsub.service.MockCategoryService;
import io.dsub.service.ModelService;
import io.dsub.util.Validator;

import java.util.Scanner;

public class AddCategoryMenu implements Menu {

    private final ModelService<Category> CATEGORY_MODEL_SERVICE = new MockCategoryService();

    @Override
    public int callMenu() {
        System.out.println("카데고리 추가");
        printCategoryList();
        addCategory();

        return backToMainMenu();
    }

    private void printCategoryList() {
        System.out.println("CategoryList");
    }

    private void addCategory() {
        System.out.println("추가하실 카테고리 명을 입력하세요");
//        String input = checkValidation(getKeyboardInput())
//        CATEGORY_MODEL_SERVICE.save(new Category(input));
        printCategoryList();
    }

    private String getKeyboardInput() {
        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();
    }

    private String checkValidation(String input) {
        if(Validator.isValidCategoryInput(input)) {
            return input;
        } else {
            System.out.println("잘못된 입력입니다");
            return checkValidation(getKeyboardInput());
        }
    }


}
