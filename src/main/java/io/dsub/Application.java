package io.dsub;

import io.dsub.cui.MenuController;
import io.dsub.util.Initializer;
import org.h2.jdbc.JdbcSQLNonTransientConnectionException;

import javax.naming.InsufficientResourcesException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

/**
 * Application entry point
 * <p>
 * todo: impl menu interfacing
 */
public class Application {

    public static final String PROD_CONN_STR = "jdbc:h2:" + System.getProperty("user.dir") + File.separator + "db" + File.separator + "h2;MODE=MySQL";
    public static final String RESET_SCHEMA_SQL = "reset_schema.sql";
    public static final String SCHEMA_NAME = "ACCOUNT";

    public static void main(String[] args) {
        int retry = 0;

        while (true) {
            try {
                // initialization phase
                Initializer.init();

                // cui phase
                MenuController menuController = MenuController.getInstance();
                menuController.selectMenu();
            } catch (SQLException e) {
                if (e instanceof JdbcSQLNonTransientConnectionException) {
                    if (retry++ == 0) {
                        System.out.println("데이터 데이스 재접속중... \n1");
                        wait(1);
                    } else if (retry < 6) {
                        System.out.println(retry);
                        wait(1);
                    } else {
                        System.out.println("데이터베이스 접속 실패");
                        break;
                    }
                    continue;
                }
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                System.out.println("초기화 sql 파일을 찾을 수 없습니다.");
            } catch (InsufficientResourcesException e) {
                System.out.println("초기화 sql 파일이 비어있습니다.");
            } finally {
                closeConn();
            }
            break;
        }
        System.out.println("앱을 종료합니다.");
    }

    public static void wait(int second) {
        wait((double) second);
    }

    public static void wait(double second) {
        long millis = Math.round(second * 1000);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println("치명적 오류 발생! 앱을 종료합니다.");
            System.exit(1);
        }
    }

    private static void closeConn() {
        try {
            if (AppState.getInstance() != null && AppState.getInstance().getConn() != null) {
                AppState.getInstance().getConn().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
