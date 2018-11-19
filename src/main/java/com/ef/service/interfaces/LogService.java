package com.ef.service.interfaces;

import com.ef.domain.Log;
import com.ef.util.Duration;

import java.time.LocalDateTime;
import java.util.List;

public interface LogService {

        List<Log> parseLogFile(String pathLogFile);

        boolean saveLogs(List<Log> logs);

        List<String> getIpsForBlock(LocalDateTime startDate, Duration duration, Integer threshold);

    }
