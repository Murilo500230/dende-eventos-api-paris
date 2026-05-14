package br.com.softhouse.dende.repositories.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ConnectionPool {

    private static HikariDataSource dataSource;

    public static DataSource getDataSource(ConfigProperties props) {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getUrl());
            config.setUsername(props.getUsername());
            config.setPassword(props.getPassword());
            config.setMaximumPoolSize(props.getMaximumPoolSize());
            config.setMinimumIdle(props.getMinimumIdle());
            config.setConnectionTimeout(props.getConnectionTimeout());
            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }
}