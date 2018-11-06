package com.log.parser.dao;

import com.log.parser.domain.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Ник on 06.11.2018.
 */
public class LogDao {

    public void save(Log currentLog){
        DBConnection connection = new DBConnection();
        try (Connection con = connection.getConnection();
             PreparedStatement statement = getPreparedStatement(con, currentLog)){
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static PreparedStatement getPreparedStatement(Connection con, Log currentLog) throws SQLException {

        String query = "INSERT INTO log (id, date, ip, request, status, user_agent) VALUES(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setLong(1, 0);
        preparedStatement.setDate(2, new java.sql.Date(currentLog.getDate().getTime()));
        preparedStatement.setString(3, currentLog.getIp());
        preparedStatement.setString(4, currentLog.getStatus());
        preparedStatement.setString(5, currentLog.getUserAgent());

        return preparedStatement;
    }
}
