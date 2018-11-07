package com.log.parser.service;

import com.log.parser.dao.LogDao;
import com.log.parser.domain.Log;
import com.log.parser.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ник on 06.11.2018.
 */
public class LogService {

    private LogDao logDao = new LogDao();

    public void parseLogFile(String pathLogFile) {

        File logFile = new File(pathLogFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                parseOneLine(line);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        System.out.println("The all log data from file with path: [ " + pathLogFile + " ] have been successfully " +
                "loaded in database");
    }

    private void parseOneLine(String line) throws ParseException {
        String[] logLine = line.split("\\|");
        if (logLine.length == 5) {
            Log currentLog = new Log();
            SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            currentLog.setLogDate(logDateFormat.parse(logLine[0]).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            currentLog.setIp(logLine[1]);
            currentLog.setRequest(logLine[2]);
            currentLog.setStatus(logLine[3]);
            currentLog.setUserAgent(logLine[4]);

            logDao.save(currentLog);
        }
    }

    public List<String> findAllLogIpsByParams(LocalDateTime startDate, Duration duration, Integer threshold) {

        List<String> foundIps = new ArrayList<>();
        LocalDateTime endDate = getEndDate(startDate, duration);
        if (endDate != null) {
            foundIps = logDao.findAllIpsByParams(startDate, endDate, threshold);
        }
        return foundIps;
    }

    private LocalDateTime getEndDate(LocalDateTime startDate, Duration duration) {
        LocalDateTime endDate = null;
        if (duration == Duration.DAILY) {
            endDate = startDate.plusHours(24);
        } else if (duration == Duration.HOURLY) {
            endDate = startDate.plusHours(1);
        }
        return endDate;
    }

    public void printResultToConsol(List<String> foundLogIps) {

        System.out.println("\t\tThe RESULTS:");
        for (String foundLogIp : foundLogIps) {
            System.out.println(foundLogIp);
        }
    }
}
