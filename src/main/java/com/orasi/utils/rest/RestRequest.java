/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.squareup.okhttp.Response;
import java.util.Map;

/**
 *
 * @author Brian Becker
 */
public interface RestRequest {
    
    public static enum RequestType {
        GET, POST, PUT, PATCH, DELETE, COPY, HEAD, OPTIONS,
        LINK, UNLINK, PURGE, LOCK, UNLOCK, PROPFIND
    }
    
    public static enum RequestFormat {
        URLENCODE, MULTIPART_FORM, RAW
    }
    
    public static class RequestData {
        public String key;
        public String value;
        public String type;
        public boolean enabled;
    }    
    
    public abstract ExpectedResponse response(String name);
    public abstract Response send() throws Exception;
    
    public abstract RestRequest env(Map variables);
    public abstract RestRequest params(String... args);
    public abstract RestRequest files(String... files);
    
}
