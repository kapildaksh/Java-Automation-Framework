package com.orasi.utils.rest;

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
        
        private String key;
        private String value;
        private String type;
        private String name;
        private Boolean enabled;

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
    
    private static class PostmanEnvironmentData {
        private String id;
        private String name;
        private Set<PostmanEnvironmentEntry> values;        
        private Number timestamp;
        private Boolean synced;
        private String syncedFilename;
    }
    
    private final PostmanEnvironmentData data;
    
    private PostmanEnvironment(PostmanEnvironmentData data) {
        this.data = data;
    }
        
    public static Map file(URL environment) throws Exception {
        return new PostmanEnvironment(Json.map.readValue(Okio.buffer(Okio.source((InputStream)environment.getContent())).readByteArray(), PostmanEnvironmentData.class));
    }    
    
    @Override
    public Set entrySet() {
        return this.data.values;
    }
    
}
