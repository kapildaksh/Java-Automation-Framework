/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 *
 * @author Brian Becker
 */
public class OkRestRequest extends RestRequest {

    private final Request req;
    
    public OkRestRequest(Request request) {
        this.req = request;
    }
    
    @Override
    public ExpectedResponse response(String name, BaseExpectedNode node) {
        throw new UnsupportedOperationException("ExpectedResponse Not Valid on Traditional Requests"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response send() throws Exception {
        OkHttpClient client = new OkHttpClient();
        client.setCookieHandler(session().getCookieManager());
        return client.newCall(req).execute();
    }
    
}
