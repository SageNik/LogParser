package com.ef.dao.interfaces;

import com.ef.domain.BlockedIp;

import java.util.List;

public interface BlockedIpDao {

    boolean saveBlockedIp(List<BlockedIp> ipsForBlock);

}
