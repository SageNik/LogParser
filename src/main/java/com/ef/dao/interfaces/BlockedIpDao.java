package com.ef.dao.interfaces;

import com.ef.domain.BlockedIp;

import java.util.List;

/**
 * This class is used for interaction with database for domain {@link BlockedIp}
 */
public interface BlockedIpDao {

    /**
     * Saving list of domain entities into database
     * @param ipsForBlock domain entities {@link BlockedIp} for save
     * @return true if successfully saved or false if not
     */
    boolean saveBlockedIp(List<BlockedIp> ipsForBlock);

    /**
     * Getting all blocked ips from database
     * @return list of found domain entities {@link BlockedIp}
     */
    List<BlockedIp> findAll();

}
