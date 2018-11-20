package com.ef.dao.interfaces;

import com.ef.domain.Log;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is used for interaction with database for domain {@link Log}
 */
public interface LogDao {

    /**
     * Saving list of domain entities into database
     * @param logs domain entities {@link Log} for save
     * @return true if successfully saved or false if not
     */
    boolean saveLogs(List<Log> logs);

    /**
     * Getting from database ip addresses by incoming parameters
     * @param startDate start date of required range
     * @param endDate end date of required range
     * @param threshold number of requests above witch ip will be blocked
     * @return list of found ips
     */
    List<String> findAllIpsByParams(LocalDateTime startDate, LocalDateTime endDate, Integer threshold);
}
