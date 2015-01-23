package com.orasi.utils.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

/**
 * Static instances of ObjectMapper components, as well as other components
 * which are safe to be shared as single static instances. 
 * 
 * @author Brian Becker
 */
public class Json {
    
    public static final ObjectMapper Map;
    
    static {
        Map = new ObjectMapper();
        Map.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        Map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Map.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
    }
    
}
