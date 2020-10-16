package com.ip.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ip.api.dto.IpInfoDTO;
import com.ip.api.dto.IpInfoResponseDTO;
import com.ip.api.exception.IpInfoServiceException;

/**
 * @author cristian.espitia
 */
public interface IpInfoService {

    IpInfoResponseDTO queryIpInfo(IpInfoDTO ipInfoDTO) throws IpInfoServiceException, JsonProcessingException;
}
