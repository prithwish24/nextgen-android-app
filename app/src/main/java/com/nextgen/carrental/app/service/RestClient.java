package com.nextgen.carrental.app.service;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextgen.carrental.app.bo.BaseResponse;
import com.nextgen.carrental.app.bo.ZipCodeResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

/**
 * Rest service client
 *
 * @author Prithwish
 */

public enum RestClient {
    INSTANCE;

    private final String TAG = RestClient.class.getName();
    private final int DEFAULT_SERVICE_TIMEOUT = 20 * 1000;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    RestClient() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory() {
            public void setConnectTimeout(int connectTimeout) {
                super.setConnectTimeout(DEFAULT_SERVICE_TIMEOUT);
            }

            public void setReadTimeout(int readTimeout) {
                super.setReadTimeout(DEFAULT_SERVICE_TIMEOUT);
            }
        });

        mapper = new ObjectMapper();
    }

    public <T> BaseResponse<T> getRequest(String url, Class<T> clazz, Object... urlVariables) throws IOException {
        Log.d(TAG, "In RestServiceClient.getRequestForm ::");
        BaseResponse br = makeGetRequest(url, urlVariables);
        if (br.isSuccess()) {
            String temp = mapper.writeValueAsString(br.getResponse());
            T type = mapper.readValue(temp, clazz);
            br.setResponse(type);
        }
        return br;
    }

    private BaseResponse makeGetRequest(String url, Object... urlVariables) {
        Log.d(TAG, "In RestClient.makeRequestWithFormData ::");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<BaseResponse> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, BaseResponse.class, urlVariables);
        return response.getBody();
    }

    public <T> BaseResponse<T> postRequest(String url, MultiValueMap<String, String> formData, Class<T> clazz) throws IOException {
        Log.d(TAG, "In RestServiceClient.postRequestForm ::");
        BaseResponse br = makeRequestWithFormData(url, formData, HttpMethod.POST);
        if (br.isSuccess()) {
            String temp = mapper.writeValueAsString(br.getResponse());
            T type = mapper.readValue(temp, clazz);
            br.setResponse(type);
        }
        return br;
    }

    private BaseResponse makeRequestWithFormData(String url, MultiValueMap<String, String> formData, HttpMethod method) {
        Log.d(TAG, "In RestClient.makeRequestWithFormData ::");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<MultiValueMap>(formData, requestHeaders);
        ResponseEntity<BaseResponse> response = restTemplate.exchange(url, method, httpEntity, BaseResponse.class);
        return response.getBody();
    }
}
