package com.ef.dao.interfaces;

import com.ef.domain.Log;

import java.time.LocalDateTime;
import java.util.List;

public interface LogDao {

    boolean saveLogs(List<Log> logs);

    List<String> findAllIpsByParams(LocalDateTime startDate, LocalDateTime endDate, Integer threshold);
}
