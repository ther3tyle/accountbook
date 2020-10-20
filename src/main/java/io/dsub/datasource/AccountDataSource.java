package io.dsub.datasource;

import io.dsub.constants.Constants;
import org.h2.jdbcx.JdbcDataSource;

public class AccountDataSource extends JdbcDataSource {
    public AccountDataSource() {
        super();
        super.setUrl(Constants.CONN_STRING);
        super.setUser("sa");
        super.setPassword("");
    }
}
