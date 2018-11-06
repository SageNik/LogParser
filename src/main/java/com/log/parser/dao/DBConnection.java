package com.log.parser.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Ник on 05.11.2018.
 */
public class DBConnection {


        private static DBConnection dataSource;
        private ComboPooledDataSource comboPooledDataSource;

        private DBConnection() {

            FileInputStream in = null;
            Connection con = null;

            try {
                Properties props = getAppProperties();

                String driver = props.getProperty("jdbc.driver");
//                if (driver != null) {
//                    Class.forName(driver);
//                }

                String url = props.getProperty("jdbc.url");
                String username = props.getProperty("jdbc.username");
                String password = props.getProperty("jdbc.password");

                comboPooledDataSource = new ComboPooledDataSource();
                comboPooledDataSource
                        .setDriverClass(driver);
                comboPooledDataSource
                        .setJdbcUrl(url);
                comboPooledDataSource.setUser(username);
                comboPooledDataSource.setPassword(password);

            } catch (IOException | PropertyVetoException e) {
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
                con = comboPooledDataSource.getConnection();
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
