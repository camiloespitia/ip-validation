package com.ip.api.service;

import com.ip.api.exception.IpValidationServiceException;

/**
 * @author cristian.espitia
 */
public interface IpValidationService {

    boolean isInBlackList(String ip);

    String addToBlackList(String ip) throws IpValidationServiceException;
}
