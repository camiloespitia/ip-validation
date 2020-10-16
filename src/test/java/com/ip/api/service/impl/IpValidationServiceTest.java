package com.ip.api.service.impl;

import com.ip.api.entity.BlackList;
import com.ip.api.exception.IpValidationServiceException;
import com.ip.api.repository.BlackListRepository;
import com.ip.api.service.IpValidationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author cristian.espitia
 */
@RunWith(MockitoJUnitRunner.class)
public class IpValidationServiceTest {

    @InjectMocks
    private IpValidationServiceImpl ipValidationService;

    @Mock
    private BlackListRepository blackListRepository;

    @Test
    public void whenisInBlackList_thenReturnTrue() {
        String ip = "10.10.10.10";
        Mockito.when(blackListRepository.getFirstByIpValue(Mockito.anyString())).thenReturn(BlackList.builder().ipValue(ip).build());
        boolean result = ipValidationService.isInBlackList(ip);
        Assert.assertTrue(result);
    }

    @Test
    public void whenisInBlackList_thenReturnFalse() {
        String ip = "10.10.10.10";
        Mockito.when(blackListRepository.getFirstByIpValue(Mockito.anyString())).thenReturn(null);
        boolean result = ipValidationService.isInBlackList(ip);
        Assert.assertFalse(result);
    }

    @Test
    public void whenaddToBlackList_thenReturnFalse() throws IpValidationServiceException {
        String ip = "10.10.10.10";
        Mockito.when(blackListRepository.saveAndFlush(Mockito.any())).thenReturn(BlackList.builder().ipValue(ip).build());
        String result = ipValidationService.addToBlackList(ip);
        Assert.assertNotNull(result);
    }
}
