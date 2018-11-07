package com.log.parser.dao;

import com.log.parser.domain.Log;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ник on 06.11.2018.
 */
public class LogDao {

    public void save(Log currentLog){
        DBConnection connection = DBConnection.getInstance();
        try (Connection con = connection.getConnection();
             PreparedStatement statement = getSavePreparedStatement(con, currentLog)){
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement getSavePreparedStatement(Connection con, Log currentLog) throws SQLException {

        String query = "INSERT INTO logs.log (id, log_date, ip, request, status, user_agent) VALUES(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setLong(1, 0);
        preparedStatement.setTimestamp(2, Timestamp.valueOf(currentLog.getLogDate()));
        preparedStatement.setString(3, currentLog.getIp());
        preparedStatement.setString(4,currentLog.getRequest());
        preparedStatement.setString(5, currentLog.getStatus());
        preparedStatement.setString(6, currentLog.getUserAgent());

        return preparedStatement;
    }

    public List<String> findAllIpsByParams(LocalDateTime startDate, LocalDateTime endDate, Integer threshold) {

        List<String> foundIps = new ArrayList<>();
        DBConnection connection = DBConnection.getInstance();
        try (Connection con = connection.getConnection();
             PreparedStatement statement = getSearchIpPreparedStatement(con, startDate, endDate, threshold);
             ResultSet resultSet = statement.executeQuery()){

            while (resultSet.next()){
                foundIps.add(resultSet.getString("ip"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundIps;
    }

    private PreparedStatement getSearchIpPreparedStatement(Connection con, LocalDateTime startDate,
                                                           LocalDateTime endDate, Integer threshold)
            throws SQLException {

        String query = "SELECT ip FROM logs.log where log_date between ? and ? group by ip having count(request) >=?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, Timestamp.valueOf(startDate).toString());
                preparedStatement.setString(2, Timestamp.valueOf(endDate).toString());
                preparedStatement.setInt(3, threshold);
                return preparedStatement;
    }
}
