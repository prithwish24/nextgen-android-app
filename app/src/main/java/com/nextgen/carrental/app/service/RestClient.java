package com.nextgen.carrental.app.service;

import android.support.annotation.NonNull;
import android.util.Log;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Collections;

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
    //private final ObjectMapper mapper;

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

        //mapper = new ObjectMapper();
    }

    @Deprecated
    public <P, Q> Q getRequest(
            @NonNull String url,
            @NonNull RestParameter<P> params,
            @NonNull ParameterizedTypeReference<Q> responseType) throws RestException {
        Log.d(TAG, "In RestServiceClient.getRequestForm ::");
        try {
            Q br = makeCall(url, params, responseType, HttpMethod.GET);
            //R br = makeGetRequest(url, params, responseType);
            /*if (r instanceof BaseResponse) {
                BaseResponse br = BaseResponse.class.cast(r);
                if (br.isSuccess()) {
                    String temp = mapper.writeValueAsString(br.getResponse());
                    T type = mapper.readValue(temp, clazz);
                    br.setResponse(type);
                }
            }*/

            return br;

        }catch (Exception e) {
            throw new RestException("GET service call failed !", e);
        }
    }

    /*public <T> T getRequest(String url, T type, RestParameter params) throws RestException {
        Log.d(TAG, "In RestServiceClient.getRequestForm ::");
        try {
            return makeGetRequest(url, params, new ParameterizedTypeReference<T>() {});
            *//*if (br.isSuccess()) {
                String temp = mapper.writeValueAsString(br.getResponse());
                T type = mapper.readValue(temp, clazz);
                br.setResponse(type);
            }
            return br;*//*

        }catch (Exception e) {
            throw new RestException("GET service call failed !", e);
        }
    }*/

    @Deprecated
    private <P, R> R makeGetRequest(
            @NonNull String url,
            @NonNull RestParameter<P> params,
            @NonNull ParameterizedTypeReference<R> responseType) {
        Log.d(TAG, "In RestClient.makeRequestWithFormData ::");
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML));

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params.hasQueryParam())
            builder.queryParams(params.getQueryParams());

        if (params.hasPathParam())
            builder.buildAndExpand(params.getPathParams());

        //requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> httpEntity = new HttpEntity<>(requestHeaders);
        final ResponseEntity<R> response = restTemplate.exchange(builder.toUriString(),
                HttpMethod.GET, httpEntity, responseType);
        return response.getBody();
    }

    @Deprecated
    public <P, Q> Q postRequest(
            @NonNull String url,
            @NonNull RestParameter<P> params,
            @NonNull ParameterizedTypeReference<Q> responseType) throws RestException {
        Log.d(TAG, "In RestServiceClient.postRequestForm ::");
        try {
            return makeRequestWithFormData(url, params, responseType);
        } catch (Exception e) {
            throw new RestException("POST service call failed !", e);
        }
    }

    /*public <T, R> BaseResponse<T> postRequest(String url, Class<T> clazz, RestParameter<R> params) throws RestException {
        Log.d(TAG, "In RestServiceClient.postRequestForm ::");
        try {
            BaseResponse<T> br = makeRequestWithFormData(url, params, HttpMethod.POST, clazz);
            if (br.isSuccess()) {
                String temp = mapper.writeValueAsString(br.getResponse());
                T type = mapper.readValue(temp, clazz);
                br.setResponse(type);
            }
            return br;

        } catch (Exception e) {
            throw new RestException("POST service call failed !", e);
        }
    }*/

    @Deprecated
    private <P, Q> Q makeRequestWithFormData(
            @NonNull String url,
            @NonNull RestParameter<P> params,
            @NonNull ParameterizedTypeReference<Q> responseType) {
        Log.d(TAG, "In RestClient.makeRequestWithFormData ::");
        HttpHeaders requestHeaders = new HttpHeaders();
        //requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        if (params.getRequestBody() == null)
            Log.w(TAG, "!! Calling post method with empty body !!");

        if (params.hasPathParam())
            builder.buildAndExpand(params.getPathParams());

        HttpEntity<P> httpEntity = new HttpEntity<>(params.getRequestBody(), requestHeaders);
        ResponseEntity<Q> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseType);
        return response.getBody();
    }


    // ------------------------------------------ NEW METHODS -------------------------------------------------------- //

    public <P, Q> Q GET(
            @NonNull String url,
            @NonNull RestParameter<P> params,
            @NonNull ParameterizedTypeReference<Q> responseType) throws RestException {
        Log.d(TAG, "In RestServiceClient.get() ::");
        try {
            return makeCall(url, params, responseType, HttpMethod.GET);
        } catch (Exception e) {
            throw new RestException("GET service call failed !", e);
        }
    }

    public <P, Q> Q POST(
            @NonNull String url,
            @NonNull RestParameter<P> params,
            @NonNull ParameterizedTypeReference<Q> responseType) throws RestException {
        Log.d(TAG, "In RestServiceClient.post() ::");
        try {
            return makeCall(url, params, responseType, HttpMethod.POST);
        } catch (Exception e) {
            throw new RestException("POST service call failed !", e);
        }
    }

    private <P, Q> Q makeCall (@NonNull final String url,
                               @NonNull final RestParameter<P> params,
                               @NonNull final ParameterizedTypeReference<Q> responseType,
                               @NonNull final HttpMethod method)
            throws Exception {

        Log.d(TAG, "In RestClient.makeCall");
        final HttpHeaders requestHeaders = new HttpHeaders();
        final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        HttpEntity<P> httpEntity;

        if (params.hasQueryParam())
            builder.queryParams(params.getQueryParams());

//        if (params.hasPathParam())
//            builder.buildAndExpand(params.getPathParams());

        if (method == HttpMethod.GET) {

            requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            httpEntity = new HttpEntity<>(requestHeaders);

        } else if (method == HttpMethod.POST) {

            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpEntity = new HttpEntity<>(params.getRequestBody(), requestHeaders);

        } else {
            throw new RestException("Unsupported request type "+method);
        }

        String finalUrl = params.hasPathParam() ? builder.buildAndExpand(params.getPathParams()).toUriString():builder.toUriString();
        final ResponseEntity<Q> response = restTemplate.exchange (
                finalUrl, method, httpEntity, responseType);
        return response.getBody();
    }
}
