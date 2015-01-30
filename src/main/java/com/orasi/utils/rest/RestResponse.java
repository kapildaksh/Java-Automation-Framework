package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;

/**
 * A generic Response object which represents a real, returned REST response.
 * 
 * @author Brian Becker
 */
public abstract class RestResponse {
    
    public abstract String data();
    public abstract int code();
    
    public boolean success() {
        return this.code() >= 200 && this.code() <= 300;
    }
    
    public JsonNode json() {
        try {
            return Json.Map.readTree(this.data());
        } catch (IOException ex) {
            // We just couldn't read it as JSON?
        }
        return new TextNode(this.data());
    }
    
}
