package io.dsub.cui.menu;

import io.dsub.AppState;
import io.dsub.Application;
import io.dsub.constants.DataType;
import io.dsub.constants.UIString;
import io.dsub.util.Initializer;

import javax.naming.InsufficientResourcesException;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.sql.Statement;

public class InitializationMenu implements Menu {
    @Override
    public int call() {
        try {
            System.out.println(UIString.INIT_ATTEMPT);

            Statement stmt = AppState.getInstance().getConn().createStatement();
            stmt.addBatch("DROP TABLE " + Application.SCHEMA_NAME + DataType.TRANSACTION.getTableName());
            stmt.addBatch("DROP TABLE " + Application.SCHEMA_NAME + DataType.VENDOR.getTableName());
            stmt.addBatch("DROP TABLE " + Application.SCHEMA_NAME + DataType.CATEGORY.getTableName());
            stmt.addBatch("DROP SCHEMA account");
            stmt.executeBatch();

            AppState.getInstance().getConn().close();
            Initializer.init();

            System.out.println(UIString.INIT_COMPLETE + "\n");
        } catch (FileNotFoundException | SQLException | InsufficientResourcesException e) {
            System.out.println("초기화 오류 발생: " + e.getMessage());
            System.out.println("앱을 종료합니다."); // TODO: impl retry
        }
        return backToMainMenu();
    }
}
