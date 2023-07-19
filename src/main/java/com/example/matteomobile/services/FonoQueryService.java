package com.example.matteomobile.services;

import com.example.matteomobile.models.DeviceInfo;
import com.example.matteomobile.models.FonaResponseBean;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@Service
public class FonoQueryService
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${fonoapi.tokengen.uri}")
    private String FONOAPI_TOKEN_GEN_URI;

    @Value("${fonoapi.base.uri}")
    private String FONOAPI_BASE_URI;

    private final String GET_DEVICE = "/getdevice";

    private RestTemplate restTemplate;

    public FonoQueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getAuthToken()
    {
        logger.debug("get fonoAPI token ");
        final String DEFAULT_TOKEN = "1q2w3e$";
        try
        {  //curl https://fonoapi.freshpixl.com/token/generate -XGET -H 'Accept: application/json'
            //Rest call 1 (Blocking Operation)
            return restTemplate.getForObject(FONOAPI_TOKEN_GEN_URI, String.class);
        }
        catch (HttpClientErrorException e)
        {   logger.info("INPUT ERROR : Invalid input, cause  " + e.getMessage()); return DEFAULT_TOKEN; }
        catch (RestClientResponseException | ResourceAccessException e)
        {  logger.info("Response timeout : Failed to get authorization token because  " + e.getMessage());
            return DEFAULT_TOKEN;
        }
    }

    public Mono<DeviceInfo> postForDeviceInfo(String name, String brand)
    {
        logger.debug("get device characteristics from fonaAPI  begins");
        ResponseEntity<FonaResponseBean> response;
        final String TECH = "850 MHz";
        final String _2G_BAND= "1900 MHz";
        final String _3G_BAND = "850 MHz";
        final String _4G_BAND = "2300 MHz";
        final String AND = "&";
        DeviceInfo defaultInfo = DeviceInfo.builder().deviceName(name).brand(brand)
                .technology(TECH)._2g_bands(_2G_BAND)
                ._3g_bands(_3G_BAND)._4g_bands(_4G_BAND).build();
        try
        {  //curl https://fonoapi.freshpixl.com/v1/getdevice -XPOST
            // -H 'Accept: application/json' -d 'token=YOUR_TOKEN_HERE&limit=5&device=A8'
            //Blocking Operation for the next call
            final String AUTH_TOKEN = getAuthToken();
            final String reqStr = new StringBuilder("token=").append(AUTH_TOKEN)
                    .append(AND).append("device=").append(name)
                    .append(AND).append("brand=").append(brand).toString();
            //Rest call 2
            response = restTemplate.postForEntity(FONOAPI_BASE_URI + GET_DEVICE , reqStr, FonaResponseBean.class);
            return Mono.just(
                    DeviceInfo.builder().deviceName(name).brand(brand)
                    .technology(TECH)._2g_bands(response.getBody().get_2g_bands())
                    ._3g_bands(response.getBody().get_3g_bands())
                    ._4g_bands(response.getBody().get_4g_bands()).build()
            );
        }
        catch (HttpClientErrorException e)
        {   logger.info("INPUT ERROR : Invalid input, cause  " + e.getMessage()); return Mono.just(defaultInfo); }
        catch (RestClientResponseException | ResourceAccessException e)
        {  logger.info("Response timeout : Failed to get remote resource because  " + e.getMessage());
            return Mono.just(defaultInfo);
        }

    }

}
