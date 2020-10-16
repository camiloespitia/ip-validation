package com.ip.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ip.api.dto.BanIpDTO;
import com.ip.api.dto.IpInfoDTO;
import com.ip.api.dto.IpInfoResponseDTO;
import com.ip.api.exception.IpInfoServiceException;
import com.ip.api.exception.IpValidationServiceException;
import com.ip.api.service.IpInfoService;
import com.ip.api.service.IpValidationService;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author cristian.espitia
 */
@RunWith(MockitoJUnitRunner.class)
public class IpControllerTest {

    @InjectMocks
    private IpController ipController;

    @Mock
    private IpInfoService ipInfoService;

    @Mock
    private IpValidationService ipValidationService;

    @Test
    public void givenRequest_whenIpController_ThenReturnIpINfo() throws IpInfoServiceException, JsonProcessingException {
        IpInfoDTO ipInfoDTO = IpInfoDTO.builder().currencyBase("EUR").ipValue("125.20.1.24").build();
        IpInfoResponseDTO ipInfoResponseDTO = IpInfoResponseDTO.builder().countryIso("GER").countryName("Germany").currencyValue("1").countryCurrency("EUR").build();
        DeferredResult<ResponseEntity<IpInfoResponseDTO>> result = ipController.getInfoIp(ipInfoDTO);
        Assert.assertNull(result.getResult());
    }

    @Test
    public void givenRequest_whenIpController_ThenBanIp() throws IpInfoServiceException, JsonProcessingException, IpValidationServiceException {
        BanIpDTO banIpDTO = BanIpDTO.builder().ipValue("125.20.1.24").build();
        Mockito.when(ipValidationService.addToBlackList(banIpDTO.getIpValue())).thenReturn(banIpDTO.getIpValue());
        ResponseEntity<String> result = ipController.banIp(banIpDTO);
        Assert.assertNotNull(result.getBody());
        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
