package com.ef.dao;

import com.zaxxer.hikari.HikariDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Ник on 05.11.2018.
 */
public class DBConnection {


        private static DBConnection dataSource;
        private HikariDataSource hikariDataSource;

        private DBConnection() {

            try {
                Properties props = getAppProperties();

                String url = props.getProperty("jdbc.url");
                String username = props.getProperty("jdbc.username");
                String password = props.getProperty("jdbc.password");

                hikariDataSource = new HikariDataSource();
                hikariDataSource.setJdbcUrl(url);
                hikariDataSource.setUsername(username);
                hikariDataSource.setPassword(password);
                hikariDataSource.setMaximumPoolSize(10);
                hikariDataSource.setMinimumIdle(2);
                hikariDataSource.addDataSourceProperty("cachePrepStmts", "true");
                hikariDataSource.addDataSourceProperty("prepStmtCacheSize", "250");
                hikariDataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                hikariDataSource.addDataSourceProperty("useServerPrepStmts", "true");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static DBConnection getInstance() {
            if (dataSource == null)
                dataSource = new DBConnection();
            return dataSource;
        }

        public Connection getConnection() {
            Connection con = null;

            try {
                con = hikariDataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return con;
        }

    private Properties getAppProperties() throws IOException {
        Properties prop = new Properties();
        String propFileName = "config.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        }
        return prop;
    }
}
