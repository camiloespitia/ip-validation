package com.ip.api.service.impl;

import com.ip.api.dto.IpInfoDTO;
import com.ip.api.dto.IpInfoResponseDTO;
import com.ip.api.exception.IpInfoServiceException;
import com.ip.api.service.IpValidationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author cristian.espitia
 */
@RunWith(MockitoJUnitRunner.class)
@Ignore
public class IpInfoServiceImplTest {

    @InjectMocks
    private IpInfoServiceImpl ipInfoService;

    @Mock
    private IpValidationService ipValidationService;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUpMock() {
        ReflectionTestUtils.setField(ipInfoService, "currencyValueURL", "http://data.fixer.io/api/latest");
        ReflectionTestUtils.setField(ipInfoService, "currencyURLAccessKey", "7223879ca13e8a5ba32b9016fcfd8893");
        ReflectionTestUtils.setField(ipInfoService, "currencyURL", "https://restcountries.eu/rest/v2/name/");
        ReflectionTestUtils.setField(ipInfoService, "countryURL", "https://api.ip2country.info/ip");
    }

    @Test
    public void givenParams_WhenQueryIpInfoAndDontInBlackList_thenReturnIPInfo() throws IpInfoServiceException {
        IpInfoDTO ipInfoDTO = IpInfoDTO.builder().ipValue("10.10.10.10").build();
        IpInfoResponseDTO ipInfoResponseDTO = IpInfoResponseDTO.builder().countryCurrency("EUR").currencyValue("1").countryName("Germany").countryIso("DE").build();
        UriComponents builder = UriComponentsBuilder.fromHttpUrl(ipInfoService.getCurrencyValueURL())
                .queryParam("access_key", ipInfoService.getCurrencyURLAccessKey())
                .queryParam("symbols", ipInfoResponseDTO.getCountryCurrency()).build();
        Mockito.when(ipValidationService.isInBlackList(ipInfoDTO.getIpValue())).thenReturn(false);
        Mockito.when(restTemplate.exchange(ipInfoService.getCountryURL() + "?" + ipInfoDTO.getIpValue(), HttpMethod.GET, null, String.class)).thenReturn(this.getResponseCountry());
        Mockito.when(restTemplate.exchange(ipInfoService.getCurrencyURL() + ipInfoResponseDTO.getCountryName() + "?fields={fields}", HttpMethod.GET, null, String.class, "currencies")).thenReturn(this.getResponseCurrency());
        Mockito.when(restTemplate.exchange(builder.toString(), HttpMethod.GET, null, String.class)).thenReturn(this.getCurrencyValue());
        IpInfoResponseDTO result = ipInfoService.queryIpInfo(ipInfoDTO);
        Assert.assertEquals(result, ipInfoResponseDTO);

    }

    private ResponseEntity<String> getResponseCurrency() {
        return new ResponseEntity<>("[\n" +
                "  {\n" +
                "    \"currencies\": [\n" +
                "      {\n" +
                "        \"code\": \"EUR\",\n" +
                "        \"name\": \"Euro\",\n" +
                "        \"symbol\": \"€\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]", HttpStatus.OK);
    }

    private ResponseEntity<String> getResponseCountry() {
        return new ResponseEntity<>("{\n" +
                "  \"countryCode\": \"DE\",\n" +
                "  \"countryCode3\": \"DEU\",\n" +
                "  \"countryName\": \"Germany\",\n" +
                "}", HttpStatus.OK);
    }

    private ResponseEntity<String> getCurrencyValue() {
        return new ResponseEntity<>("{\n" +
                "  \"success\": true,\n" +
                "  \"timestamp\": 1602880445,\n" +
                "  \"base\": \"EUR\",\n" +
                "  \"date\": \"2020-10-16\",\n" +
                "  \"rates\": {\n" +
                "    \"EUR\": 1\n" +
                "  }\n" +
                "}", HttpStatus.OK);
    }
}
