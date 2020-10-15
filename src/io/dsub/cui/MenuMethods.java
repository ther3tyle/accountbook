package io.dsub.cui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuMethods {


    public static void  selectMenu(MenuType menu){

        if(menu == MenuType.INCOME){
            inputIncome();
        } else if(menu == MenuType.EXPENSE){
            inputExpense();
        } else if(menu == MenuType.CHECK){
            checkAccount();
        } else if(menu == MenuType.CATEGORY){
            addCategory();
        } else if(menu== MenuType.INITIALIZATION){
            initialization();
        } else if(menu== MenuType.EXIT){
            exitAccountBook();
        }
    }




    public static List<String> inputIncome() {
        Scanner scanner = new Scanner(System.in);
        List<String> list = new ArrayList<>();

        System.out.println("수입 입력");
        while (list.size() < 4) {

            if(list.size()!=0) {
                System.out.println("입력하신 내용");
                System.out.println(list.toString());
            }

            if (list.size() == 0) {
                System.out.println("거래날짜를 입력해주세요(이전단계: p 메인메뉴: q)");
            } else if (list.size() == 1) {
                System.out.println("금액을 입력해주세요(이전단계: p 메인메뉴: q)");
            } else if (list.size() == 2) {
                System.out.println("사용처를 입력해주세요(이전단계: p 메인메뉴: q)");
            } else {
                System.out.println("카테고리를 입력해주세요(이전단계: p 메인메뉴: q)");
            }

            String input = scanner.nextLine();
            if(input.equals("p")||input.equals("P")) {
                if(list.size()!=0){
                    list.remove(list.size()-1);
                    System.out.println("이전 단계로");
                }
            } else if(input.equals("q")||input.equals("Q") ) {
                return null;
            } else {
                list.add(input);
            }
        }


        System.out.println(list.toString());
        System.out.println("입력이 끝났습니다");
        return list;
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

    public static void initialization() {
        System.out.println("초기화");
    }

    public static void exitAccountBook(){
        System.out.println("가계부를 종료합니다");
        System.exit(0);
    }
}
