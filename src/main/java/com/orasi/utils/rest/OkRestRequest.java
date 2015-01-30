/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * The OkRestRequest is an implementation of RestRequest which uses the
 * OkHttp Request object to send the data and receive its response. It
 * returns a RestResponse upon sending.
 * 
 * @author Brian Becker
 */
public class OkRestRequest extends RestRequest {

    private final Request req;
    
    /**
     * Get a new OkRestRequest from an OkHttp Request.
     * 
     * @param request 
     */
    public OkRestRequest(Request request) {
        this.req = request;
    }
    
    /**
     * Get a "Dummy Response" from this request. The name of the request
     * is simply the actual response body.
     * 
     * @param name
     * @param node
     * @return 
     */
    @Override
    public ExpectedResponse response(String name, BaseExpectedNode node) {
        return new ResponseVerifier(this, name, node);
    }

    /**
     * Send the Request and return a RestResponse upon receiving a real
     * response. This is intended to normalize any API between different
     * HTTP Clients.
     * 
     * @return
     * @throws Exception 
     */
    @Override
    public RestResponse send() throws Exception {
        OkHttpClient client = new OkHttpClient();
        client.setCookieHandler(session().getCookieManager());
        return new OkRestResponse(client.newCall(req).execute());
    }
    
}
