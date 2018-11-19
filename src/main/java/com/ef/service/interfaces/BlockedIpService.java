package com.ef.service.interfaces;

import com.ef.domain.BlockedIp;

import java.util.List;

public interface BlockedIpService {

    List<BlockedIp> getBlockedIps(List<String> ipsForBlock, String duration, Integer threshold);
    void printResultToConsol(List<BlockedIp> foundLogIps);
    boolean saveBlockedIps(List<BlockedIp> blockedIps);
}
