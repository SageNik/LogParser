package com.ef.service.impl;

import com.ef.dao.interfaces.LogDao;
import com.ef.service.interfaces.LogService;
import com.ef.util.Duration;
import com.ef.domain.Log;
import lombok.extern.slf4j.Slf4j;

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
 * {@inheritDoc}
 */
@Slf4j
public class LogServiceImpl implements LogService {

    private LogDao logDao;

    private final SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final String IP_ADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
             "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
             "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private final String STATUS_PATTERN = "^\\d{3}";

    public LogServiceImpl(LogDao logDao) {
        this.logDao = logDao;
    }

    @Override
    public List<Log> parseLogFile(String pathLogFile) {
        System.out.println("Start to parse log file with path "+pathLogFile+". It takes some time.....");

        List<Log> parsedLogs = new ArrayList<>();
        File logFile = new File(pathLogFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
               Log currentLog = parseOneLine(line);
               if(currentLog != null) {
                   parsedLogs.add(currentLog);
               }
               else{
                   log.error("Error! The current line hasn't been parsed as Log. line: [ "+line+" ]");
               }
            }
        } catch (IOException ignore) {
            log.info("Error. The file with path: [ "+pathLogFile+" ] hasn't been found");
        }catch (ParseException ignore) {
            log.info("Error. The file with path: [ "+pathLogFile+" ] hasn't been parsed correctly");
        }
        log.info("The all log data from file with path: [ " + pathLogFile + " ] have been successfully " +
                "parsed with size: [ "+parsedLogs.size()+" ]");
        return parsedLogs;
    }

    @Override
    public boolean saveLogs(List<Log> logs) {
        return logDao.saveLogs(logs);
    }

    @Override
    public List<String> getIpsForBlock(LocalDateTime startDate, Duration duration, Integer threshold) {
        LocalDateTime endDate = getEndDate(startDate, duration);
        List<String> foundIps = new ArrayList<>();
        log.debug("Start getting ip by threshold= "+threshold+" for a period from " + startDate + " to " + endDate);
        if (endDate != null) {
            foundIps = logDao.findAllIpsByParams(startDate, endDate, threshold);
        }
        return foundIps;
    }

    private Log parseOneLine(String line) throws ParseException {

        String[] logLine = line.split("\\|");
        if (logLine.length == 5) {
            LocalDateTime date = logDateFormat.parse(logLine[0]).toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime();
            String ip = (logLine[1].matches(IP_ADDRESS_PATTERN))? logLine[1] : null;
            String request = logLine[2];
            Integer status = (logLine[3].matches(STATUS_PATTERN))? Integer.parseInt(logLine[3]) : null;
            String userAgent = logLine[4];
            Log currentLog = null;

            if(date != null && ip != null && request != null && status != null && userAgent != null) {
                currentLog = new Log(date, ip, request, status, userAgent);
            }
            return currentLog;
        }
        return null;
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
}
