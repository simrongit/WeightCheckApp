package com.tools.simron.weightcheckapp.rest;

import com.tools.simron.weightcheckapp.WeightInfo;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by simro on 11/4/2017.
 */

public class ProductRestClient {

    private static final String BASE_URL = "http://localhost:8080/lw/lastEntry";
    private RestTemplate restTemplate = new RestTemplate();

    public WeightInfo find(int id){
        try {
            HttpEntity<String> stringHttpEntity = new HttpEntity<>("simron");
            User user = new User();
            user.setUserId("simron");
            return restTemplate.postForObject(BASE_URL, user, WeightInfo.class);
        }catch (Exception e){
            return new WeightInfo();
        }
//        restTemplate.exchange("getAll", HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
//        }).getBody();

    }

}
