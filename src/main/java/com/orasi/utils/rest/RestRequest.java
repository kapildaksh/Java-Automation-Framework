package com.orasi.utils.rest;

import java.util.Arrays;
import java.util.List;
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
    
    private Map environment;
    private Map params;
    private List<String> files;
    private RestSession session;
    
    /**
     * All of the valid, and somewhat common, request types. All request
     * types should be specified in this enumeration, even the less-used
     * ones.
     */
    public static enum RequestType {
        GET, POST, PUT, PATCH, DELETE, COPY, HEAD, OPTIONS,
        LINK, UNLINK, PURGE, LOCK, UNLOCK, PROPFIND
    }
    
    /**
     * The valid request formats for a request. They currently consist of
     * URL encoded, a multi part form, and a raw message body. The first
     * two are for form data, and some web services might still utilize
     * this, as no scripting engine is required to make a POST request with
     * form data. Raw data is generated from JavaScript or any other source,
     * which may be JSON, XML, plain text, or any other format.
     */
    public static enum RequestFormat {
        URLENCODE, MULTIPART_FORM, RAW
    }
    
    /**
     * RequestData is specifically key-value for form data. The type is only
     * used when it comes to multi part forms. Enabled is just a flag which
     * can be used to send or not send the data. Key is the parameter name
     * and value is the value of the given parameter.
     */
    public static class RequestData {
        public String key;
        public String value;
        public String type;
        public boolean enabled;
    }    
    
    /**
     * Get an ExpectedResponse referred to by the specified name in the
     * request information. This is either a response which has been returned
     * from the server after the request, or one which has been manually
     * entered according to requirements.
     * 
     * @param   name        The response name
     * @return  An expected response for the specified name
     */
    public ExpectedResponse response(String name) {
        return this.response(name, new BaseExpectedNode());
    }
    
    /**
     * This is the response method which should be implemented by all
     * concrete REST request classes. It takes a parameter of a
     * BaseExpectedNode, the start of the node tree, so that an external
     * client can pass in a node of its own and perform actions on it.
     * 
     * @param   name    The response name
     * @param   node    BaseExpectedNode which should be passed to the response
     * @return 
     */
    public abstract ExpectedResponse response(String name, BaseExpectedNode node);
    
    /**
     * Send the REST request, and return the message which was retrieved
     * back as a RestResponse object. This is for a request which does not
     * need to be validated, but where it is still convenient to use the
     * collection system to send the response.
     * 
     * @return  Rest response
     * @throws Exception 
     */
    public abstract RestResponse send() throws Exception;
    
    /**
     * Set an environment (list of variables) on the request. This
     * environment is a local set of variables which can be called
     * on by entering specifiers such as {{var}} into the URL or
     * any request. Unfortunately, no variable substitution in requests
     * yet, as postman doesn't output this type of file.
     * 
     * @param   variables   List of variables used to populate the request
     * @return  this
     */    
    public RestRequest env(Map variables) {
        this.environment = variables;
        return this;
    }
    
    /**
     * Gets the environment of the request. The variables are used in
     * the body, the url, etc.
     * 
     * @return  The variables which are being used as the current environment
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
     * @param   args        The url parameters for the request
     * @return  this
     */
    public RestRequest params(Map args) {
        this.params = args;
        return this;
    }
    
    /**
     * Get the parameters list for building the REST request.
     * 
     * @return  Map of parameters
     */
    public Map params() {
        return params;
    }    

    /**
     * Set a bunch of files to be used with this REST request. Files are
     * specified in Postman, but they must be selected on every run of
     * a file. Therefore they are available for replacement programmatically.
     * For other data sources, the files should consist of any placeholder
     * which is a file type that isn't otherwise specified.
     * 
     * @param   files       The files which should be used for multi form
     * @return  this
     */    
    public RestRequest files(String... files) {
        this.files = Arrays.asList(files);
        return this;
    }
    
    /**
     * Get the list of files which were set on this request. These are used
     * by the request itself to read the files and send them in the request.
     * 
     * @return  The list of files which should be used for multi form
     */
    public List<String> files() {
        return this.files;
    }    
    
    /**
     * Set the request's session. The session is the outside environment or
     * stateful behavior that this request exists in. For authenticated
     * requests, sessions are useful to hold variables and cookies.
     * 
     * @param   session    A new session for this request
     * @return  this
     */
    public RestRequest session(RestSession session) {
        this.session = session;
        return this;
    }
    
    /**
     * Retrieve the request's session.
     * 
     * @return  Current request's session.
     */
    public RestSession session() {
        return this.session;
    }
    
}
