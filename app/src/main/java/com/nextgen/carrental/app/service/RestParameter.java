package com.nextgen.carrental.app.service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Hold parameters for the rest service call
 * @author Prithwish
 */

public class RestParameter<T> {

    private Map <String, String> qp;
    private Map <String, String> pp;
    private T rq;

    public RestParameter() {
        qp = new HashMap<>();
        pp = new HashMap<>();
    }

    public void addQueryParam(String k, String v) {
        qp.put(k, v);
    }

    public void addPathParam(String k, String v) {
        pp.put(k, v);
    }

    public Map<String, String> getPathParams() {
        return pp;
    }

    public MultiValueMap<String, String> getQueryParams() {
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.setAll(qp);
        return mvm;
    }

    public boolean hasQueryParam() {
        return !qp.isEmpty();
    }

    public boolean hasPathParam() {
        return !pp.isEmpty();
    }

    public void addBody(T t) {
        this.rq = t;
    }

    public T getRequestBody() {
        return rq;
    }
}
