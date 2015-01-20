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
    
    public static void assertInArray(JsonNode container, Object... contained) throws Exception {
        ObjectMapper map = new ObjectMapper();
        RestAssert.assertIsArray(container);
        for (Object c : contained) {
            boolean found = false;
            ArrayNode an = (ArrayNode) container;
            for(int i = 0; i < an.size(); i++) {
                found = found ? true : map.writeValueAsString(an.get(i)).equals(map.writeValueAsString(c));
            }
            if(!found) {
                Assert.fail("Item [" + c.toString() + "] not found in array node, should be in array.");
            }
        }
    }
    
    public static void assertNotInArray(JsonNode container, Object... contained) throws Exception {
        ObjectMapper map = new ObjectMapper();
        RestAssert.assertIsArray(container);
        for (Object c : contained) {
            boolean found = false;
            ArrayNode an = (ArrayNode) container;
            for(int i = 0; i < an.size(); i++) {
                found = found ? true : map.writeValueAsString(an.get(i)).equals(map.writeValueAsString(c));
            }
            if(found) {
                Assert.fail("Item [" + c.toString() + "] found in array node, should not be in array.");
            }          
        }
    }
    
    public static void assertIsArray(JsonNode container) {
        if(!(container instanceof ArrayNode)) {
            Assert.fail("Array node expected, got " + container.getNodeType().name() + ".");
        }
    }
    
    public static void assertNotArray(JsonNode container) {
        if(container instanceof ArrayNode) {
            Assert.fail("Non-array node expected, got array node.");
        }        
    }
    
}
