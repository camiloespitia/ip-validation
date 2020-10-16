package com.ip.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ip.api.constant.Constants;
import com.ip.api.dto.IpInfoDTO;
import com.ip.api.dto.IpInfoResponseDTO;
import com.ip.api.exception.IpInfoServiceException;
import com.ip.api.service.IpInfoService;
import com.ip.api.service.IpValidationService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * @author cristian.espitia
 */
@Slf4j
@Service
@Data
public class IpInfoServiceImpl implements IpInfoService {

    @Value("${currency.value.url}")
    private String currencyValueURL;

    @Value("${currency.value.url.access_key}")
    private String currencyURLAccessKey;

    @Value("${currency.url}")
    private String currencyURL;

    @Value("${country.url}")
    private String countryURL;

    private IpValidationService ipValidationService;

    @Autowired
    public IpInfoServiceImpl(IpValidationService ipValidationService) {
        this.ipValidationService = ipValidationService;
    }

    @Override
    public IpInfoResponseDTO queryIpInfo(IpInfoDTO ipInfoDTO) throws IpInfoServiceException {
        if (ipValidationService.isInBlackList(ipInfoDTO.getIpValue())) {
            throw new IpInfoServiceException("The IP is on a blacklist.");
        }
        IpInfoResponseDTO ipInfoResponseDTO = this.getCountryByIp(ipInfoDTO.getIpValue());
        ipInfoResponseDTO.setCountryCurrency(this.getCurrencyByCountryName(ipInfoResponseDTO.getCountryName()));
        ipInfoResponseDTO.setCurrencyValue(this.getCurrencyValue(ipInfoResponseDTO.getCountryCurrency(), ipInfoDTO.getCurrencyBase()));

        return ipInfoResponseDTO;
    }

    @Cacheable(cacheNames="country")
    public IpInfoResponseDTO getCountryByIp(String ip) throws IpInfoServiceException {
        try {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange(countryURL+"?"+ip, HttpMethod.GET, null, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode countryName = root.path("countryName");
            JsonNode countryCode = root.path("countryCode");
            return IpInfoResponseDTO.builder().countryName(countryName.asText()).countryIso(countryCode.asText()).build();
        } catch (HttpStatusCodeException | JsonProcessingException exception) {
            log.error(exception.getMessage());
            throw new IpInfoServiceException(String.format(Constants.LOG_ERROR_SERVICE, countryURL));
        }
    }
    @Cacheable(cacheNames="currency")
    public String getCurrencyByCountryName(String countryName) throws IpInfoServiceException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(currencyURL+countryName+"?fields={fields}", HttpMethod.GET, null, String.class, "currencies");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody()).get(0);
            JsonNode currencies = root.get("currencies").get(0);
            return currencies.get("code").asText();
        } catch (HttpStatusCodeException | JsonProcessingException exception) {
            log.error(exception.getMessage());
            throw new IpInfoServiceException(String.format(Constants.LOG_ERROR_SERVICE, currencyURL));
        }
    }

    /*
    The free version of the api is based on EUR, even so it is parameterized in case you want to use the full version
     */
    @Cacheable(cacheNames="currencyValue")
    public String getCurrencyValue(String currency, String currencyBase) throws IpInfoServiceException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            UriComponents builder = UriComponentsBuilder.fromHttpUrl(currencyValueURL)
//                .queryParam("base",currencyBase)
                    .queryParam("access_key", currencyURLAccessKey)
                    .queryParam("symbols", currency).build();

            ResponseEntity<String> response = restTemplate.exchange(builder.toString(), HttpMethod.GET, null, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("rates").get(currency).asText();
        } catch (HttpStatusCodeException | JsonProcessingException exception) {
            log.error(exception.getMessage());
            throw new IpInfoServiceException(String.format(Constants.LOG_ERROR_SERVICE, currencyValueURL));
        }
    }
}
