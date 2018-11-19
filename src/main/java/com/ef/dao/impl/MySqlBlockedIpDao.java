package com.ef.dao.impl;


import com.ef.dao.interfaces.BlockedIpDao;
import com.ef.domain.BlockedIp;

import java.util.List;

public class MySqlBlockedIpDao implements BlockedIpDao{
    @Override
    public boolean saveBlockedIp(List<BlockedIp> blockedIps) {


        return false;
    }
}
