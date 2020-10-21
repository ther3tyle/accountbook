package io.dsub.datasource;

import io.dsub.constants.StringConstants;
import org.h2.jdbcx.JdbcDataSource;

public class AccountDataSource extends JdbcDataSource {
    public AccountDataSource() {
        this(StringConstants.CONN_STRING);
    }

    public AccountDataSource(String url, String user, String password) {
        super();
        super.setURL(url);
        super.setUser(user);
        super.setPassword(password);
    }

    public AccountDataSource(String url) {
        this(url, "sa", "");
    }

    public AccountDataSource(String url, String user) {
        this(url, user, "");
    }
}
