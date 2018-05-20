package com.nextgen.carrental.app.service;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextgen.carrental.app.bo.BaseResponse;

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
import org.springframework.web.util.UriComponentsBuilder;

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

    @SuppressWarnings("unchecked")
    public <T> BaseResponse<T> getRequest(String url, Class<T> clazz, RestParameter params) throws RestException {
        Log.d(TAG, "In RestServiceClient.getRequestForm ::");
        try {
            BaseResponse<T> br = makeGetRequest(url, params);
            if (br.isSuccess()) {
                String temp = mapper.writeValueAsString(br.getResponse());
                T type = mapper.readValue(temp, clazz);
                br.setResponse(type);
            }
            return br;

        }catch (Exception e) {
            throw new RestException("GET service call failed !", e);
        }
    }

    private BaseResponse makeGetRequest(String url, RestParameter<?> params) {
        Log.d(TAG, "In RestClient.makeRequestWithFormData ::");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params != null && params.hasQueryParam())
            builder.queryParams(params.getQueryParams());

        if (params != null && params.hasPathParam())
            builder.buildAndExpand(params.getPathParams());

        //requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(requestHeaders);
        ResponseEntity<BaseResponse> response = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, httpEntity, BaseResponse.class);
        return response.getBody();
    }

    @SuppressWarnings("unchecked")
    public <T, R> BaseResponse<T> postRequest(String url, Class<T> clazz, RestParameter<R> params) throws RestException {
        Log.d(TAG, "In RestServiceClient.postRequestForm ::");
        try {
            BaseResponse<T> br = makeRequestWithFormData(url, params, HttpMethod.POST);
            if (br.isSuccess()) {
                String temp = mapper.writeValueAsString(br.getResponse());
                T type = mapper.readValue(temp, clazz);
                br.setResponse(type);
            }
            return br;

        } catch (Exception e) {
            throw new RestException("POST service call failed !", e);
        }
    }

    private <R> BaseResponse makeRequestWithFormData(String url, RestParameter<R> params, HttpMethod method) {
        Log.d(TAG, "In RestClient.makeRequestWithFormData ::");
        HttpHeaders requestHeaders = new HttpHeaders();
        //requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params == null || params.getRequestBody() == null)
            Log.w(TAG, "!! Calling post method with empty body !!");

        if (params != null && params.hasPathParam())
            builder.buildAndExpand(params.getPathParams());

        HttpEntity<R> httpEntity = new HttpEntity<>(params.getRequestBody(), requestHeaders);
        ResponseEntity<BaseResponse> response = restTemplate.exchange(url, method, httpEntity, BaseResponse.class);
        return response.getBody();
    }
}
