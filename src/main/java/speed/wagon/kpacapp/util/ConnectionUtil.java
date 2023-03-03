package speed.wagon.kpacapp.util;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConnectionUtil {
    private final Environment environment;

    public ConnectionUtil(Environment environment) {
        this.environment = environment;
    }

    public Connection getConnection() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("db.driver"));
        dataSource.setUrl(environment.getProperty("db.url"));
        dataSource.setUsername(environment.getProperty("db.user"));
        dataSource.setPassword(environment.getProperty("db.password"));
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Cant create connection to Db", e);
        }
    }
}
