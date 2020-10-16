package com.ip.api.service.impl;

import com.ip.api.entity.BlackList;
import com.ip.api.exception.IpValidationServiceException;
import com.ip.api.repository.BlackListRepository;
import com.ip.api.service.IpValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author cristian.espitia
 */
@Service
public class IpValidationServiceImpl implements IpValidationService {

    private BlackListRepository blackListRepository;

    @Autowired
    public IpValidationServiceImpl(BlackListRepository blackListRepository) {
        this.blackListRepository = blackListRepository;
    }

    @Override
    public boolean isInBlackList(String ip) {
        return Optional.ofNullable(blackListRepository.getFirstByIpValue(ip)).isPresent();
    }

    @Override
    public String addToBlackList(String ip) throws IpValidationServiceException {
        return Optional.of(blackListRepository.saveAndFlush(BlackList.builder().ipValue(ip).build()).getIpValue()).orElseThrow(() -> new IpValidationServiceException("Could not add IP to blacklist"));
    }
}
