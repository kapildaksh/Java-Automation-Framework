package com.orasi.utils.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Okio;

/**
 * This file reads the Postman Environment files which can be saved during
 * manual testing of a REST service. They contain the replacements for
 * variables held in the {{ and }} tags. This is a generic map type and can
 * be used with any methods available for a standard map.
 * 
 * @author Brian Becker
 */
public class PostmanEnvironment extends AbstractMap {
    
    /**
     * This is an environment entry, basically just a key value store in 
     * JSON. This specifies all the variables and values, and you can
     * use these just like a regular map entry in a map.
     */
    public static class PostmanEnvironmentEntry implements Map.Entry<String, String> {
        
        private String key;
        private String value;

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public String setValue(String value) {
            String old = this.value;
            this.value = value;
            return old;
        }
        
    }
    
    /**
     * A bunch of data regarding the entire environment file, there is an id
     * as well as a name, as well as the list of values, and some other
     * bookkeeping functions.
     */
    private static class PostmanEnvironmentData {
        private Set<PostmanEnvironmentEntry> values;        
    }
    
    private final PostmanEnvironmentData data;
    
    /**
     * Creates a new Postman Environment based on a some deserialized JSON
     * data.
     * 
     * @param data 
     */
    private PostmanEnvironment(PostmanEnvironmentData data) {
        this.data = data;
    }
    
    /**
     * Build a Postman Environment from a file, either a local file or a file
     * on an accessible URL.
     * 
     * @param environment
     * @return 
     */
    public static Map file(URL environment) {
        try {
            return new PostmanEnvironment(Json.Map.readValue(Okio.buffer(Okio.source((InputStream)environment.getContent())).readByteArray(), PostmanEnvironmentData.class));
        } catch (IOException ex) {
            throw new RuntimeException("Postman Environment File Invalid");
        }
    }    
    
    /**
     * This is the key-value store for the Postman Environment. As we are
     * using an AbstractMap, all functions are based on the Entry Set.
     * 
     * @return 
     */
    @Override
    public Set entrySet() {
        return this.data.values;
    }
    
}
