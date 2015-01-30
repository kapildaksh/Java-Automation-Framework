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
public class OkRestResponse extends RestResponse {

    private final Response res;
    private String data;
    private int status;
    
    public OkRestResponse(Response response) {
        this.res = response;
        try {
            this.data = response.body().string();
            this.status = response.code();
        } catch (Exception e) {
            this.status = -1;
        }
    }

    @Override
    public String data() {
        return this.data;
    }

    @Override
    public int code() {
        return this.status;
    }
        
}
