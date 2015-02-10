/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.orasi.utils.rest.RestRequest.RequestData;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This is a helper class which helps to build REST Request objects for the
 * OkHttp HTTP library.
 * 
 * @author Brian Becker
 */
public class RestRequestBuilder {
    
    private RestRequest.RequestType method = RestRequest.RequestType.GET;
    private RestRequest.RequestFormat format = RestRequest.RequestFormat.RAW;
    private List<RequestData> data = new LinkedList<RequestData>();
    private String rawData = "";
    private Map helperAttributes = new HashMap();
    private List<String> files = new LinkedList<String>();
    private final Request.Builder builder = new Request.Builder();
    private Object headers;
    private String url;
    
    public RestRequestBuilder() {
    }
    
    public RestRequestBuilder method(RestRequest.RequestType method) {
        this.method = method;
        return this;
    }
    
    public RestRequestBuilder format(RestRequest.RequestFormat format) {
        this.format = format;
        return this;
    }
    
    public RestRequestBuilder data(List<RequestData> data, String rawData) {
        this.rawData = rawData;
        this.data = data;
        return this;
    }
    
    public RestRequestBuilder files(List<String> files) {
        this.files = files;
        return this;
    }
    
    public RestRequestBuilder auth(Map helperAttributes) {
        this.helperAttributes = helperAttributes;
        return this;
    }
    
    public RestRequestBuilder url(String url) {
        this.url = url;
        return this;
    }
    
    public RestRequestBuilder headers(Object headers) {
        this.headers = headers;
        return this;
    }
    
    public Request build() throws Exception {
        builder.url(RestRequestHelpers.url(method, url, data));
        if(headers instanceof String) {
            builder.headers(RestRequestHelpers.headers((String)headers));
        } else if(headers instanceof Headers) {
            builder.headers((Headers)headers);
        }
        if(method.equals(RestRequest.RequestType.GET)) {
            builder.get();
        } else {
            builder.method(method.toString(), RestRequestHelpers.body(method, format, data, rawData, files));
        }
        return builder.build();
    }
    
}
