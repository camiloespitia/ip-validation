package com.ip.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ip.api.constant.ResourceMapping;
import com.ip.api.dto.BanIpDTO;
import com.ip.api.dto.IpInfoDTO;
import com.ip.api.dto.IpInfoResponseDTO;
import com.ip.api.exception.IpInfoServiceException;
import com.ip.api.exception.IpValidationServiceException;
import com.ip.api.service.IpInfoService;
import com.ip.api.service.IpValidationService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cristian.espitia
 */
@RestController
public class IpController {

    private IpInfoService ipInfoService;

    private IpValidationService ipValidationService;

    @Autowired
    public IpController(IpInfoService ipInfoService, IpValidationService ipValidationService) {
        this.ipInfoService = ipInfoService;
        this.ipValidationService = ipValidationService;
    }

    @GetMapping(value = ResourceMapping.GET_INFO_IP)
    public ResponseEntity<IpInfoResponseDTO> getInfoIp(@RequestBody @NonNull IpInfoDTO ipInfoDTO) throws IpInfoServiceException, JsonProcessingException {
        return new ResponseEntity<>(ipInfoService.queryIpInfo(ipInfoDTO), HttpStatus.OK);
    }

    @PostMapping(value = ResourceMapping.BAN_IP)
    public ResponseEntity banIp(@RequestBody @NonNull BanIpDTO banIpDTO) throws IpValidationServiceException {
        return new ResponseEntity<>(ipValidationService.addToBlackList(banIpDTO.getIpValue()), HttpStatus.OK);
    }
}
