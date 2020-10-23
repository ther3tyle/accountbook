import io.dsub.AppState;
import org.junit.jupiter.api.AfterAll;

import java.sql.SQLException;

public class TestMain {
    @AfterAll
    static void cleanUp() throws SQLException {
        AppState.getInstance().getConn().close();
    }
}
