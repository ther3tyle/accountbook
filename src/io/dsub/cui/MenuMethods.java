package io.dsub.cui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MenuMethods {
    public static void selectMenu(MenuType menu) {

        // INCOME 과 EXPENSE 는 같은 메서드 공유
        if (menu == MenuType.INCOME || menu == MenuType.EXPENSE ) {
            inputStatementMenu(menu);
        } else if (menu == MenuType.CHECK) {
            checkAccount();
        } else if (menu == MenuType.CATEGORY) {
            addCategory();
        } else if (menu == MenuType.INITIALIZATION) {
            initialize();
        } else if (menu == MenuType.EXIT) {
            exitAccountBook();
        }
    }


    public static List<String> inputStatementMenu(MenuType menu) {
        Scanner scanner = new Scanner(System.in);
        List<String> list = new ArrayList<>();
        // 입력받는 항목 입력
        List<String> inputParams = Arrays.asList("Date","Amount","Vendor","Category");


        if (menu == MenuType.INCOME) {
            System.out.println("수입 입력");
        } else {
            System.out.println("지출 입력");
        }

        while (list.size() < inputParams.size()) {

            printStatementMenuInfo(list, inputParams);
            String input = scanner.nextLine();

            if (input.equals("p") || input.equals("P")) {
                if (list.size() != 0) {
                    list.remove(list.size() - 1);
                    System.out.println("이전 단계로");
                }
            } else if (input.equals("q") || input.equals("Q")) {
                return null;
            } else {
                if (list.size() == inputParams.indexOf("Amount") && menu == MenuType.EXPENSE) { // Expense면 금액에 '-'추가
                    String minus = "-";
                    list.add(minus + input);
                } else {
                    list.add(input);
                }
            }
        }
        // 입력값 확인하고 return
        return checkInputStatement(list, menu); // 입력값 확인할 수 있는 메서드 추가
    }

    public static void inputExpense() {
        System.out.println("지출");
    }

    public static void checkAccount() {
        System.out.println("조회");
    }

    public static void addCategory() {
        System.out.println("카테고리추가");
    }

    public static void initialize() {
        System.out.println("초기화");
    }

    public static void exitAccountBook() {
        System.out.println("가계부를 종료합니다");
        System.exit(0);
    }

    private static List<String> checkInputStatement(List<String> list, MenuType menu) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(list.toString());
        System.out.println("입력한 내용을 확인해주세요(Y/N)");
        String input = scanner.nextLine();

        if (input.equals("Y") || input.equals("y")) {
            System.out.println("입력이 완료되었습니다");
            return list;
        } else {
            inputStatementMenu(menu);
        }
        return null;
    }

    private static void printStatementMenuInfo(List<String> list, List<String> inputParams) {

        // 입력한 내용 확인
        if (list.size() != 0) {
            System.out.println("입력하신 내용");
            System.out.println(list.toString());
        }

        // 입력 받는 순서 안내
        if (list.size() == inputParams.indexOf("Date") ) {
            System.out.println("거래날짜를 입력해주세요(이전단계: p 메인메뉴: q)");
        } else if (list.size() == inputParams.indexOf("Amount")) {
            System.out.println("금액을 입력해주세요(이전단계: p 메인메뉴: q)");
        } else if (list.size() == inputParams.indexOf("Vendor")) {
            System.out.println("사용처를 입력해주세요(이전단계: p 메인메뉴: q)");
        } else if (list.size() == inputParams.indexOf("Category")) {
            System.out.println("카테고리를 입력해주세요(이전단계: p 메인메뉴: q)");
        }
    }
}
