package com.ef.service.interfaces;

import com.ef.domain.BlockedIp;

import java.util.List;

/**
 * This class is used for processing business logic for domain {@link BlockedIp}
 */
public interface BlockedIpService {

    /**
     * Getting list of domain entities {@link BlockedIp} just converting incoming data to entities
     * @param ipsForBlock lis of ips for blocking
     * @param duration set duration for blocking
     * @param threshold set threshold for blocking
     * @return list of domain entities for block
     */
    List<BlockedIp> getBlockedIps(List<String> ipsForBlock, String duration, Integer threshold);

    /**
     * Printing the results
     */
    void printResultToConsole();

    /**
     * Saving list of domain entities to database
     * @param blockedIps list of domain entities
     * @return true if saved successfully or false if not
     */
    boolean saveBlockedIps(List<BlockedIp> blockedIps);
}
