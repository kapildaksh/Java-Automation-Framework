package com.orasi.utils.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;

/**
 * Static instances of ObjectMapper components, as well as other components
 * which are safe to be shared as single static instances. These components
 * should not incur any performance penalty to be shared between all
 * instances of a given runtime.
 * 
 * Additionally, some of the components, such as the ObjectMapper, are safe
 * for use with multiple threads concurrently. However, the configuration of
 * these is not. If you need a configured variant, they must be generated
 * per-instance.
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
