package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
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
    
    public static class PostmanEnvironmentEntry implements Map.Entry<String, String> {
        
        public String key;
        public String value;
        public String type;
        public String name;
        public Boolean enabled;

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
    
    public static class PostmanEnvironmentData {
        public String id;
        public String name;
        public Set<PostmanEnvironmentEntry> values;        
        public Number timestamp;
        public Boolean synced;
        public String syncedFilename;
    }
    
    private final PostmanEnvironmentData object;
    
    public PostmanEnvironment(byte[] data) throws IOException {
        ObjectMapper map = new ObjectMapper();
        object = map.readValue(data, PostmanEnvironmentData.class);
    }    
    
    public static Map file(URL environment) throws Exception {
        return new PostmanEnvironment(Okio.buffer(Okio.source((InputStream)environment.getContent())).readByteArray());
    }    
    
    @Override
    public Set entrySet() {
        return object.values;
    }
    
}
