package com.ef.service.impl;

import com.ef.dao.interfaces.BlockedIpDao;
import com.ef.domain.BlockedIp;
import com.ef.service.interfaces.BlockedIpService;

import java.util.ArrayList;
import java.util.List;

public class BlockedIpServiceImpl implements BlockedIpService{

    private BlockedIpDao blockedIpDao;

    public BlockedIpServiceImpl(BlockedIpDao blockedIpDao) {
        this.blockedIpDao = blockedIpDao;
    }

    @Override
    public List<BlockedIp> getBlockedIps(List<String> ipsForBlock, String duration, Integer threshold) {

        List<BlockedIp> blockedIps = new ArrayList<>();
        for (String ip : ipsForBlock) {
            String reason = "Number of requests more then "+threshold+" by duration is "+duration;
            BlockedIp blockedIp = new BlockedIp(ip, reason);
            blockedIps.add(blockedIp);
        }
        return blockedIps;
    }

    @Override
    public boolean saveBlockedIps(List<BlockedIp> blockedIps) {
        return blockedIpDao.saveBlockedIp(blockedIps);
    }

    @Override
    public void printResultToConsol(List<BlockedIp> blockedIps) {
        System.out.println("\t\tThe RESULTS:");
        for (BlockedIp blockedIp : blockedIps) {
            System.out.println("\t IP: "+blockedIp.getIp()+" blocked by reason: "+blockedIp.getReason());
        }
    }
}
