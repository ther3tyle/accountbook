package io.dsub.cui.menu;

import io.dsub.Application;
import io.dsub.constants.MenuType;
import io.dsub.constants.UIString;
import io.dsub.model.Category;
import io.dsub.service.CategoryServiceImpl;
import io.dsub.service.ModelService;
import io.dsub.util.InputValidator;

import java.sql.SQLException;
import java.util.List;

public class AddCategoryMenu implements Menu {

    private final ModelService<Category> service;
    private List<Category> catList;

    public AddCategoryMenu() {
        service = new CategoryServiceImpl();
    }

    @Override
    public int call() {
        catList = service.findAll();
        System.out.printf("*-*-* %s *-*-*\n", MenuType.CATEGORY.getTitle());
        printCategoryList();
        addCategory();
        return backToMainMenu();
    }

    private void printCategoryList() {
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

    private void addCategory() {
        while (true) {
            String name = takeCategoryName();
            if (name == null) return;
            if (confirm(name)) {
                try {
                    service.save(new Category(name));
                    System.out.printf("등록되었습니다: [%s]\n\n", name);
                    Application.wait(1.7);
                } catch (SQLException e) {
                    System.out.println("등록에 실패했습니다: " + e.getLocalizedMessage());
                }
                return;
            }
        }
    }

    private boolean confirm(String value) {
        System.out.printf("입력받은 내용: \"%s\"\n%s%s\n", value, UIString.ASK_INPUT_CONFIRM, UIString.YES_OR_NO);
        while (true) {
            String in = inputHandler.take();
            if (InputValidator.matches(in, UIString.POSITIVES)) {
                return true;
            } else if (InputValidator.matches(in, UIString.NEGATIVES)) {
                return false;
            }
            System.out.println(UIString.CANNOT_RECOGNIZE + UIString.RE_ENTER_PROMPT);
        }
    }

    private String takeCategoryName() {
        System.out.println("추가하실 카테고리 명을 입력하세요. [q|quit|exit] 돌아가기");
        while (true) {
            String in = inputHandler.take();

            if (InputValidator.matches(in, UIString.EXITS)) {
                return null;
            }

            if (in.length() > 0) {
                return in;
            }

            System.out.println(UIString.INVALID_INPUT + UIString.RE_ENTER_PROMPT);
        }
    }
}
