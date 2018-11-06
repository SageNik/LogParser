package com.log.parser;

import com.log.parser.dao.DBConnection;
import com.log.parser.dao.LogDao;
import com.log.parser.domain.Log;
import com.log.parser.util.Duration;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ник on 05.11.2018.
 */
public class Main {

    public static void main(String[] args) {

        String pathLogFile = null;
        Date startDate = null;
        Duration duration = null;
        Integer threshold = null;

        String accesslogPrefix = "--accesslog=";
        String startDatePrefix = "--startDate=";
        String durationPrefix = "--duration=";
        String thresholdPrefix = "--threshold=";

        for (String arg : args) {
            if (arg.startsWith(accesslogPrefix)) {
                pathLogFile = arg.replace(accesslogPrefix, "");
            }
            if (arg.startsWith(startDatePrefix)) {
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
                String date = arg.replace(startDatePrefix, "");
                try {
                    startDate = dt.parse(date);
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

        if (pathLogFile != null && startDate != null && duration != null && threshold != null) {


            File logFile = new File(pathLogFile);
            try(BufferedReader reader = new BufferedReader(new FileReader(logFile))){
                String line = null;
                while((line = reader.readLine()) != null){
                    String[] logLine = line.split("\\|");
                    if(logLine.length == 5) {
                        Log currentLog = new Log();
                        SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        currentLog.setDate(logDateFormat.parse(logLine[0]));
                        currentLog.setIp(logLine[1]);
                        currentLog.setRequest(logLine[2]);
                        currentLog.setStatus(logLine[3]);
                        currentLog.setUserAgent(logLine[4]);

                        LogDao logDao = new LogDao();
                        logDao.save(currentLog);
                    }
                }
            }catch (IOException | ParseException ex){
                ex.printStackTrace();
            }

        }
    }
}
