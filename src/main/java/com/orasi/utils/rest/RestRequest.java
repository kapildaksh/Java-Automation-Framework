package com.orasi.utils.rest;

import java.util.Arrays;
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
    public abstract RestResponse send() throws Exception;
    
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
    
    /**
     * Gets the environment of the request. The variables are used in
     * the body, the url, etc.
     * 
     * @return 
     */
    public Map env() {
        return this.environment;
    }    

    /**
     * Set parameters on the request. These parameters determine
     * what the :value1, :var2, type parameters on the URL will
     * be replaced with. These are identical to the Spark framework
     * style parameters.
     * 
     * @param args
     * @return 
     */
    public RestRequest params(Map args) {
        this.params = args;
        return this;
    }
    
    /**
     * Apply the parameters list to the URL in an appropriate way. This
     * will match all occurrences of :test, :this, etc. The parameters
     * which have been given to this object determine the end values.
     * 
     * @param url
     * @return 
     */
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
    
    /**
     * Get the list of files which were set on this request. These are used
     * by the request itself to read the files and send them in the request.
     * 
     * @return 
     */
    public List<String> files() {
        return this.files;
    }    
    
    /**
     * Set the request's session. The session is the outside environment or
     * stateful behavior that this request exists in. For authenticated
     * requests, sessions are useful to hold variables and cookies.
     * 
     * @param session
     * @return 
     */
    public RestRequest session(RestSession session) {
        this.session = session;
        return this;
    }
    
    /**
     * Retrieve the request's session.
     * 
     * @return 
     */
    public RestSession session() {
        return this.session;
    }
    
}
