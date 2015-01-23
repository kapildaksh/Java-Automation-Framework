package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.testng.Assert;

/**
 * The RestAssert class comes with a few helpers for creating assertions, for
 * JsonNode traversal. Namely, traversing things like arrays is somewhat
 * cumbersome as opposed to objects.
 * 
 * @author Brian Becker
 */
public class RestAssert {
    
    /**
     * Require a number of objects be in an array.
     * 
     * @param container JsonNode to check
     * @param contained Objects which are required
     * @throws Exception 
     */
    public static void assertInArray(JsonNode container, Object... contained) throws Exception {
        RestAssert.assertIsArray(container);
        for (Object c : contained) {
            boolean found = false;
            ArrayNode an = (ArrayNode) container;
            for(int i = 0; i < an.size(); i++) {
                found = found ? true : Json.Map.writeValueAsString(an.get(i)).equals(Json.Map.writeValueAsString(c));
            }
            if(!found) {
                Assert.fail("Item [" + c.toString() + "] not found in array node, should be in array.");
            }
        }
    }
    
    /**
     * Require a number of objects to all not be in an array.
     * 
     * @param container JsonNode to check
     * @param contained Objects which are required
     * @throws Exception 
     */
    public static void assertNotInArray(JsonNode container, Object... contained) throws Exception {
        RestAssert.assertIsArray(container);
        for (Object c : contained) {
            boolean found = false;
            ArrayNode an = (ArrayNode) container;
            for(int i = 0; i < an.size(); i++) {
                found = found ? true : Json.Map.writeValueAsString(an.get(i)).equals(Json.Map.writeValueAsString(c));
            }
            if(found) {
                Assert.fail("Item [" + c.toString() + "] found in array node, should not be in array.");
            }          
        }
    }
    
    /**
     * Require that the JsonNode be an array.
     * 
     * @param container JsonNode to check
     */
    public static void assertIsArray(JsonNode container) {
        if(!(container instanceof ArrayNode)) {
            Assert.fail("Array node expected, got " + container.getNodeType().name() + ".");
        }
    }
    
    /**
     * Require that the JsonNode not be an array.
     * 
     * @param container JsonNode to check
     */
    public static void assertNotArray(JsonNode container) {
        if(container instanceof ArrayNode) {
            Assert.fail("Non-array node expected, got array node.");
        }        
    }
    
}
