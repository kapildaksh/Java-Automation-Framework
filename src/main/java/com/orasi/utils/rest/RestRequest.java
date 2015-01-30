package com.orasi.utils.rest;

import com.squareup.okhttp.Response;
import edu.emory.mathcs.backport.java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A RestRequest is intended to send the request to the server, with any
 * given variables, parameters, or files. It also contains a list of
 * responses, which may have been generated during manual testing or by
 * a schema writer.
 * 
 * @author Brian Becker
 */
public abstract class RestRequest {
    
    private Map environment;
    private Map params;
    private List<String> files;
    private RestSession session;
    
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
    
    /**
     * Set an environment (list of variables) on the request. This
     * environment is a local set of variables which can be called
     * on by entering specifiers such as {{var}} into the URL or
     * any request. Unfortunately, no variable substitution in requests
     * yet, as postman doesn't output this type of file.
     * 
     * @param variables
     * @return 
     */    
    public RestRequest env(Map variables) {
        this.environment = variables;
        return this;
    }

    public RestRequest params(Map args) {
        this.params = args;
        return this;
    }

    /**
     * Set a bunch of files to be used with this REST request. Files are
     * specified in Postman, but they must be selected on every run of
     * a file. Therefore they are available for replacement programmatically.
     * For other data sources, the files should consist of any placeholder
     * which is a file type that isn't otherwise specified.
     * 
     * @param files
     * @return 
     */    
    public RestRequest files(String... files) {
        this.files = Arrays.asList(files);
        return this;
    }
    
    public String applyParams(String url) {
        if(params != null) {
            for(Object e : params.entrySet()) {
                String key = ((Entry)e).getKey().toString();
                String val = ((Entry)e).getValue().toString();
                if(url.contains(":" + key)) {
                    url = url.replace(":" + key, val);
                }
            }
        }
        return url;
    }
    
    public RestRequest session(RestSession session) {
        this.session = session;
        return this;
    }
    
    public RestSession session() {
        return this.session;
    }
    
    protected Map getVariables() {
        return this.environment;
    }
    
    protected List<String> getFiles() {
        return this.files;
    }
    
}
