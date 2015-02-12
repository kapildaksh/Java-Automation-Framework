package com.orasi.utils.rest;

import javax.ws.rs.core.Response;

/**
 * This is a shim class for a RestRequest, which is necessary for holding a
 * request that has no manipulation besides the ability to affect expected
 * conditions. This allows for some fluent code in a testing environment.
 * 
 * @author Brian Becker
 */
public class RxRestRequest extends RestRequest {
    
    private final Response res;
    
    public RxRestRequest(Response res) {
        this.res = res;
    }

    @Override
    public ExpectedResponse response(String name, BaseExpectedNode node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response send() throws Exception {
        return res;
    }
    
}
