/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.Assert;

/**
 *
 * @author brian.becker
 */
public class RestAssert {
    public static void assertArrayContainsValue(JsonNode container, Object contained) throws Exception {
        ObjectMapper map = new ObjectMapper();
        
        boolean found = false;
        if(container instanceof ArrayNode) {
            ArrayNode an = (ArrayNode) container;
            for(int i = 0; i < an.size(); i++) {
                found = found ? true : map.writeValueAsString(an.get(i)).equals(map.writeValueAsString(contained));
            }
            if(!found) {
                Assert.fail("Item not found in array node.");
            }
        } else {
            Assert.fail("Array node expected.");
        }
    }
    
    public static void assertArrayNotContainsValue(JsonNode container, Object contained) throws Exception {
        ObjectMapper map = new ObjectMapper();
        
        boolean found = false;
        if(container instanceof ArrayNode) {
            ArrayNode an = (ArrayNode) container;
            for(int i = 0; i < an.size(); i++) {
                found = found ? true : map.writeValueAsString(an.get(i)).equals(map.writeValueAsString(contained));
            }
            if(found) {
                Assert.fail("Item not found in array node.");
            }
        } else {
            Assert.fail("Array node expected.");
        }
    }    
    
    public static void assertArrayContainsValues(JsonNode container, Object... contained) throws Exception {
        for (Object c : contained) {
            RestAssert.assertArrayContainsValue(container, c);
        }
    }
    
    public static void assertArrayNotContainsValues(JsonNode container, Object... contained) throws Exception {
        for (Object c : contained) {
            RestAssert.assertArrayNotContainsValue(container, c);
        }
    }
    
}
