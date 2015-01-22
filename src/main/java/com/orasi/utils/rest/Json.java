/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public static ObjectMapper MAP;
    
    static {
        MAP = new ObjectMapper();
        MAP.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        MAP.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAP.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
    }
    
}
