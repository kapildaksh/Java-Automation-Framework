/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.orasi.utils.rest.RestRequest.RequestData;
import com.squareup.okhttp.Request;
import java.util.List;
import java.util.Map;

/**
 * This is a helper class which helps to build REST Request objects for the
 * OkHttp HTTP library.
 * 
 * @author Brian Becker
 */
public class RestRequestBuilder {    
    private RestRequest.RequestType method;
    private RestRequest.RequestFormat format;
    private List<RequestData> data;
    private String rawData;
    private Map variables;
    private Map helperAttributes;
    private List<String> files;
    private final Request.Builder builder = new Request.Builder();
    
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
    
    public RestRequestBuilder variables(Map variables) {
        this.variables = variables;
        return this;
    }
    
    public RestRequestBuilder auth(Map helperAttributes) {
        this.helperAttributes = helperAttributes;
        return this;
    }
    
    public RestRequestBuilder url(String url) {
        builder.url(RestRequestHelpers.url(method, url, data, variables));
        return this;
    }
    
    public RestRequestBuilder headers(String headers) {
        builder.headers(RestRequestHelpers.headers(headers, helperAttributes, variables));
        return this;
    }
    
    public Request build() throws Exception {
        if(!method.equals(RestRequest.RequestType.GET)) {
            builder.method(method.toString(), RestRequestHelpers.body(method, format, data, rawData, variables, files));
        }
        return builder.build();
    }
}
