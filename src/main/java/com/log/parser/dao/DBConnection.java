package com.log.parser.dao;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Ник on 05.11.2018.
 */
public class DBConnection {

    private Connection connection;

    public Connection getConnection() {

        Properties prop = getAppProperties();
        try {
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(prop.getProperty("db-driver"));
            ds.setUrl(prop.getProperty("db-url"));
            ds.setUsername(prop.getProperty("db-user"));
            ds.setPassword(prop.getProperty("db-password"));
            ds.setMinIdle(5);
            ds.setMaxIdle(10);
            ds.setMaxOpenPreparedStatements(100);
            connection = ds.getConnection();
        } catch (SQLException e) {
            System.out.println("MySQL Connection Failed!");
            e.printStackTrace();
        }
        return connection;
    }

    private Properties getAppProperties() {
        Properties prop = new Properties();
        String propFileName = "/application.properties";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
                    prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
