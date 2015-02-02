package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;

/**
 * A generic Response object which represents a real, returned REST response.
 * This wrapper helps unify different types of responses, as long as there is
 * a decorator which can implement the abstract methods of this class to wrap
 * around the response.
 * 
 * @author  Brian Becker
 */
public abstract class RestResponse {
    
    /**
     * The REST call, after being sent, will have received some form of a
     * response when it has completed. The data method will allow you to
     * determine what the REST call returned with.
     * 
     * @return  Data string consisting of entire REST response body
     */
    public abstract String data();
    
    /**
     * Sometimes it is only necessary to know the return value of the HTTP
     * status code, or it may be helpful to use in conjunction with the body
     * (for instance, code should match a "soft" code). A return value of
     * -1 signifies an actual failed request, which was failed in sending and
     * the server never sent any reply.
     * 
     * @return  Actual HTTP status code received
     */
    public abstract int code();
    
    /**
     * The REST call will be said to be successful if the value of the return
     * code is somewhere within 2xx. These are the success return codes, which
     * should be received. Receiving a redirect, etc, will not return a
     * success.
     * 
     * @return  Success code of the REST call
     */
    public boolean success() {
        return this.code() >= 200 && this.code() <= 300;
    }
    
    /**
     * The JSON body of a REST request can be converted into a JSON node, if
     * the body can be parsed. This method will automatically retrieve a
     * JSON node (tree) which consists of the response in a usable form.
     * 
     * @return  JSON structure containing parsed data
     */
    public JsonNode json() {
        try {
            return Json.Map.readTree(this.data());
        } catch (IOException ex) {
            // We just couldn't read it as JSON?
        }
        return new TextNode(this.data());
    }
    
}
