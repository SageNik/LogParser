package com.ef.service.interfaces;

import com.ef.domain.Log;
import com.ef.util.Duration;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is used for processing business logic for domain {@link Log}
 */
public interface LogService {

    /**
     * Parsing log file to list of domain entities
     * @param pathLogFile path to log file for parsing
     * @return list of parsed entities
     */
     List<Log> parseLogFile(String pathLogFile);

    /**
     * Saving list of domain entities into database
     * @param logs domain entities {@link Log} for save
     * @return true if successfully saved or false if not
     */
     boolean saveLogs(List<Log> logs);

    /**
     * Getting ips which should be blocked according incoming parameters
     * @param startDate start date of required range
     * @param duration duration from start day
     * @param threshold number of requests above witch ip will be blocked
     * @return list of found ips
     */
     List<String> getIpsForBlock(LocalDateTime startDate, Duration duration, Integer threshold);

    }
