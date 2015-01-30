package com.orasi.utils.rest;

import com.squareup.okhttp.Response;
import java.util.Map;

/**
 * A RestRequest is intended to send the request to the server, with any
 * given variables, parameters, or files. It also contains a list of
 * responses, which may have been generated during manual testing or by
 * a schema writer.
 * 
 * @author Brian Becker
 */
public abstract class RestRequest {
    
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
    
    public ExpectedResponse response(String name) {
        return this.response(name, new BaseExpectedNode());
    }
    
    public abstract ExpectedResponse response(String name, BaseExpectedNode node);
    public abstract Response send() throws Exception;
    
    public abstract RestRequest env(Map variables);
    public abstract RestRequest params(Map args);
    public abstract RestRequest files(String... files);
    
}
