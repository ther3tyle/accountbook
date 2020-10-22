package io.dsub.cui.menu;

import io.dsub.constants.StringConstants;
import io.dsub.model.Category;
import io.dsub.service.CategoryServiceImpl;
import io.dsub.service.ModelService;
import io.dsub.util.Validator;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AddCategoryMenu implements Menu {

    private final ModelService<Category> CATEGORY_MODEL_SERVICE;
    private List<Category> catList;
    private static final String INVALID_INPUT = StringConstants.INVALID_INPUT;
    private static final String RE_ENTER_PROMPT = StringConstants.RE_ENTER_PROMPT;
    private static final String ASK_INPUT_CONFIRM = StringConstants.ASK_INPUT_CONFIRM;
    private static final String CANNOT_RECOGNIZE = StringConstants.CANNOT_RECOGNIZE;


    public AddCategoryMenu() {
        CATEGORY_MODEL_SERVICE = new CategoryServiceImpl();
    }

    @Override
    public int callMenu() {
        catList = CATEGORY_MODEL_SERVICE.findAll();
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

        String listStr;

        if (catList.isEmpty()) {
            listStr = "없음";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < catList.size(); i++) {
                sb.append(catList.get(i).getName());
                if (i < catList.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            listStr = sb.toString();
        }
        System.out.println(listStr);
    }

    private void addCategory() throws SQLException {
        while (true) {
            String name = takeCategoryName();
            if (confirm(name)) {

            }
        }
    }

    private boolean confirm(String value) {
        System.out.printf("[%s]", value);
        System.out.println(ASK_INPUT_CONFIRM + "[y|yes] 예 [n|no] 아니오");
        while (true) {
            String in = takeInput();
            if (Validator.matches(in, "y", "yes", "예", "네")) return true;
            if (Validator.matches(in, "n", "no", "아니오", "아니요")) return false;
            System.out.println(CANNOT_RECOGNIZE + RE_ENTER_PROMPT);
        }
    }

    private String takeCategoryName() {
        System.out.println("추가하실 카테고리 명을 입력하세요.");
        while (true) {
            String in = takeInput();
            if (in.length() > 0) {
                return in;
            }
            System.out.println(INVALID_INPUT + RE_ENTER_PROMPT);
        }
    }

    private String takeInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
