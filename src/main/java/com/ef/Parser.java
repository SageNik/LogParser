package com.ef;

import com.ef.dao.MySqlDBConnection;
import com.ef.dao.impl.MySqlBlockedIpDao;
import com.ef.dao.impl.MySqlLogDao;
import com.ef.dao.interfaces.BlockedIpDao;
import com.ef.dao.interfaces.LogDao;
import com.ef.domain.BlockedIp;
import com.ef.domain.Log;
import com.ef.service.impl.BlockedIpServiceImpl;
import com.ef.service.impl.LogServiceImpl;
import com.ef.service.interfaces.BlockedIpService;
import com.ef.service.interfaces.LogService;
import com.ef.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
public class Parser {

    public static void main(String[] args) {

        String pathLogFile = null;
        LocalDateTime startDate = null;
        Duration duration = null;
        Integer threshold = null;

        String accesslogPrefix = "--accesslog=";
        String startDatePrefix = "--startDate=";
        String durationPrefix = "--duration=";
        String thresholdPrefix = "--threshold=";

        if (args.length == 4) {
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
        } else System.out.println("Sorry, invalid incoming data");
        if (pathLogFile != null && startDate != null && duration != null && threshold != null) {
            MySqlDBConnection dbConnection= new MySqlDBConnection();
            LogDao logDao = new MySqlLogDao(dbConnection);
            LogService logService = new LogServiceImpl(logDao);
            List<Log> parseLogs = logService.parseLogFile(pathLogFile);

            if (logService.saveLogs(parseLogs)) {
                BlockedIpDao blockedIpDao = new MySqlBlockedIpDao(dbConnection);
                BlockedIpService blockedIpService = new BlockedIpServiceImpl(blockedIpDao);

                List<String> ipsForBlock = logService.getIpsForBlock(startDate, duration, threshold);
                List<BlockedIp> blockedIp = blockedIpService.getBlockedIps(ipsForBlock, duration.name(), threshold);
                blockedIpService.saveBlockedIps(blockedIp);
                blockedIpService.printResultToConsole();
            }

        } else {
            log.info("Sorry, invalid incoming data");
        }
    }
}
