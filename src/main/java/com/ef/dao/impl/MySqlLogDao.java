package com.ef.dao.impl;

import com.ef.dao.MySqlDBConnection;
import com.ef.dao.interfaces.LogDao;
import com.ef.domain.Log;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Slf4j
public class MySqlLogDao implements LogDao {

    private final MySqlDBConnection connection;

    public MySqlLogDao(MySqlDBConnection connection) {
        this.connection = connection;
    }

    @Override
    public boolean saveLogs(List<Log> logs) {

        try (Connection con = connection.getConnection()) {
            save(con, logs);
            return true;
        } catch (SQLException ignore) {
            log.error("Error. Any logs have not been saved");
            return false;
        }
    }

    @Override
    public List<String> findAllIpsByParams(LocalDateTime startDate, LocalDateTime endDate, Integer threshold) {

        List<String> foundIps = new ArrayList<>();
        try (Connection con = connection.getConnection();
             PreparedStatement statement = getSearchIpPreparedStatement(con, startDate, endDate, threshold);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                foundIps.add(resultSet.getString("ip"));
            }
        } catch (SQLException e) {
            log.error("Error. Failed to get a logs. Error: "+e.getMessage());
        }
        return foundIps;
    }

    private PreparedStatement save(Connection con, List<Log> currentLogs) throws SQLException {

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
            preparedStatement.setInt(5, currentLog.getStatus());
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
