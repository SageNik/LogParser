package com.ef.dao.impl;

import com.ef.dao.MySqlDBConnection;
import com.ef.dao.interfaces.BlockedIpDao;
import com.ef.domain.BlockedIp;
import lombok.extern.slf4j.Slf4j;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Slf4j
public class MySqlBlockedIpDao implements BlockedIpDao{

    private final MySqlDBConnection connection;

    public MySqlBlockedIpDao(MySqlDBConnection connection) {
        this.connection = connection;
    }

    @Override
    public boolean saveBlockedIp(List<BlockedIp> blockedIps) {
        try (Connection con = connection.getConnection()){
             getSaveBlockedStatement(con, blockedIps);
            return true;
        } catch (SQLException e) {
            log.error("Error. Any logs have not been saved. Error: "+e.getMessage());
            return false;
        }
    }

    @Override
    public List<BlockedIp> findAll() {
            List<BlockedIp> blockedIps = new ArrayList<>();
            String query = "Select * from logs.blocked";
            try (Connection con = connection.getConnection();
                 PreparedStatement statement = con.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    BlockedIp blockedIp = getBlockedIp(resultSet);
                    blockedIps.add(blockedIp);
                }
            } catch (SQLException e) {
                log.error("Error. Failed to get a blocked ips. Error: "+e.getMessage());
            }
            return blockedIps;
        }

    private BlockedIp getBlockedIp(ResultSet resultSet) throws SQLException {
        BlockedIp blockedIp = new BlockedIp();
        blockedIp.setId(resultSet.getLong("id"));
        blockedIp.setIp(resultSet.getString("ip"));
        blockedIp.setReason(resultSet.getString("reason"));
        return blockedIp;
    }

    private void getSaveBlockedStatement(Connection con, List<BlockedIp> blockedIps) throws SQLException {

        String query = "INSERT INTO logs.blocked (id, ip, reason) VALUES(?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(query);

        final int batchSize = 1000;
        int count = 0;
        con.setAutoCommit(false);
        for (BlockedIp blockedIp : blockedIps) {

            preparedStatement.setLong(1, 0);
            preparedStatement.setString(2, blockedIp.getIp());
            preparedStatement.setString(3, blockedIp.getReason());
            preparedStatement.addBatch();

            if (++count % batchSize == 0) {
                preparedStatement.executeBatch();
                con.commit();
                preparedStatement.clearBatch();
            }
        preparedStatement.executeBatch();
        con.commit();
        preparedStatement.close();
        con.setAutoCommit(true);
        }
    }
}
