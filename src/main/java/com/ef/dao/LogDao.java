package com.ef.dao;

import com.ef.domain.Log;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ник on 06.11.2018.
 */
public class LogDao {

    public void save(List<Log> currentLogs) {
        DBConnection connection = DBConnection.getInstance();
        try (Connection con = connection.getConnection()) {
            saveLogs(con, currentLogs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> findAllIpsByParams(LocalDateTime startDate, LocalDateTime endDate, Integer threshold) {

        List<String> foundIps = new ArrayList<>();
        DBConnection connection = DBConnection.getInstance();
        try (Connection con = connection.getConnection();
             PreparedStatement statement = getSearchIpPreparedStatement(con, startDate, endDate, threshold);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                foundIps.add(resultSet.getString("ip"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundIps;
    }

    public void saveBlocked(String ip, String reason) {
        DBConnection connection = DBConnection.getInstance();
        try (Connection con = connection.getConnection();
             PreparedStatement statement = getSaveBlockedStatement(con, ip, reason)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement getSaveBlockedStatement(Connection con, String ip, String reason) throws SQLException {

        String query = "INSERT INTO logs.blocked (id, ip, reason) VALUES(?,?,?)";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, 0);
        statement.setString(2, ip);
        statement.setString(3, reason);
        return statement;
    }

    private PreparedStatement saveLogs(Connection con, List<Log> currentLogs) throws SQLException {

        String query = "INSERT INTO logs.log (id, log_date, ip, request, status, user_agent) VALUES(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(query);

        final int batchSize = 1000;
        int count = 0;
        con.setAutoCommit(false);

        for (Log currentLog : currentLogs) {
            preparedStatement.setLong(1, 0);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(currentLog.getLogDate()));
            preparedStatement.setString(3, currentLog.getIp());
            preparedStatement.setString(4, currentLog.getRequest());
            preparedStatement.setString(5, currentLog.getStatus());
            preparedStatement.setString(6, currentLog.getUserAgent());
            preparedStatement.addBatch();

            if (++count % batchSize == 0) {
                preparedStatement.executeBatch();
                con.commit();
                preparedStatement.clearBatch();
            }
        }
        preparedStatement.executeBatch();
        con.commit();
        preparedStatement.close();
        con.setAutoCommit(true);

        return preparedStatement;
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
