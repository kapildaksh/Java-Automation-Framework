package com.orasi.utils.rest;

/**
 * The NullRestRequest is a placeholder value where a RestRequest must be
 * returned because of the possibility of returning a bad value, but the
 * operation must notify the programmer of the issue.
 * 
 * @author Brian Becker
 */
public class NullRestRequest extends RestRequest {
    
    @Override
    public ExpectedResponse response(String name, BaseExpectedNode node) {
        throw new UnsupportedOperationException("Not supported for a null request."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public RestResponse send() throws Exception {
        throw new UnsupportedOperationException("Not supported for a null request."); //To change body of generated methods, choose Tools | Templates.
    }
            
}
