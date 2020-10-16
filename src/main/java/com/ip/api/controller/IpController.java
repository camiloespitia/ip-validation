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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ForkJoinPool;

/**
 * @author cristian.espitia
 */
@Slf4j
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
    public DeferredResult<ResponseEntity<IpInfoResponseDTO>> getInfoIp(@RequestBody @NonNull IpInfoDTO ipInfoDTO) {
        DeferredResult<ResponseEntity<IpInfoResponseDTO>> output = new DeferredResult<>();
        ForkJoinPool.commonPool().submit(() -> {
            log.info("Processing get info ip request");
            IpInfoResponseDTO ipInfoResponseDTO = null;
            try {
                ipInfoResponseDTO = ipInfoService.queryIpInfo(ipInfoDTO);
            } catch (IpInfoServiceException | JsonProcessingException e) {
                output.setErrorResult(e);
            }
            output.setResult(new ResponseEntity<>(ipInfoResponseDTO, HttpStatus.OK));
        });
        return output;
    }

    @PostMapping(value = ResourceMapping.BAN_IP)
    public ResponseEntity<String> banIp(@RequestBody @NonNull BanIpDTO banIpDTO) throws IpValidationServiceException {
        return new ResponseEntity<>(ipValidationService.addToBlackList(banIpDTO.getIpValue()), HttpStatus.OK);
    }
}
