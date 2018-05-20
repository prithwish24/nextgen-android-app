package com.nextgen.carrental.app.service;

/**
 *  Custom exception for reset service call.
 *  @author Prithwish
 */

public class RestException extends RuntimeException {
    public RestException(String m) {
        super(m);
    }
    public RestException(String m, Exception rte) {
        super(m, rte);
    }
    public RestException(Exception rte) {
        super(rte);
    }
}
