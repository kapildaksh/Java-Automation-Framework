/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.squareup.okhttp.Response;

/**
 * OkRestResponse is a RestResponse which is implemented using an OkHttp
 * Response object. This particular implementation caches the body().string()
 * for convenience so that it's not lost unintentionally, it stores the code
 * from HTTP response.
 * 
 * @author Brian Becker
 */
public class OkRestResponse extends RestResponse {

    private final Response res;
    private String data;
    private int status;
    
    /**
     * Get a new OkRestResponse from an OkHttp Response
     * 
     * @param response 
     */
    public OkRestResponse(Response response) {
        this.res = response;
        try {
            this.data = response.body().string();
            this.status = response.code();
        } catch (Exception e) {
            this.status = -1;
        }
    }

    /**
     * Get the entire body of received data.
     * 
     * @return 
     */
    @Override
    public String data() {
        return this.data;
    }

    /**
     * Get the error (or success) code.
     * 
     * @return 
     */
    @Override
    public int code() {
        return this.status;
    }
        
}