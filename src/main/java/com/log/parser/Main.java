package com.log.parser;

import com.log.parser.dao.DBConnection;
import com.log.parser.dao.LogDao;
import com.log.parser.domain.Log;
import com.log.parser.service.LogService;
import com.log.parser.util.Duration;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by Ник on 05.11.2018.
 */
public class Main {

    public static void main(String[] args) {

        String pathLogFile = null;
        LocalDateTime startDate = null;
        Duration duration = null;
        Integer threshold = null;
        LogService logService = new LogService();

        String accesslogPrefix = "--accesslog=";
        String startDatePrefix = "--startDate=";
        String durationPrefix = "--duration=";
        String thresholdPrefix = "--threshold=";

        if(args.length == 4) {
            for (String arg : args) {
                if (arg.startsWith(accesslogPrefix)) {
                    pathLogFile = arg.replace(accesslogPrefix, "");
                }
                if (arg.startsWith(startDatePrefix)) {
                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
                    String date = arg.replace(startDatePrefix, "");
                    try {
                        startDate = dt.parse(date).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (arg.startsWith(durationPrefix)) {
                    String dur = arg.replace(durationPrefix, "");
                    if ("hourly".equals(dur)) duration = Duration.HOURLY;
                    else if ("daily".equals(dur)) duration = Duration.DAILY;
                }
                if (arg.startsWith(thresholdPrefix)) {
                    threshold = Integer.parseInt(arg.replace(thresholdPrefix, ""));
                }
            }
        }else System.out.println("Sorry, invalid incoming data");
        if (pathLogFile != null && startDate != null && duration != null && threshold != null) {

//            logService.parseLogFile(pathLogFile);
            List<String> foundLogIps = logService.findAllLogIpsByParams(startDate,duration, threshold);
            logService.printResultToConsol(foundLogIps);

        }else System.out.println("Sorry, invalid incoming data");
    }
}
