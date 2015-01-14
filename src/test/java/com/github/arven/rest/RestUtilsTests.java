/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arven.rest.util.Patch;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class RestUtilsTests {
    
    private final ObjectMapper map;
    
    public RestUtilsTests() {
        this.map = new ObjectMapper();
    }
    
    @Test
    public void buildPatchTest() throws Exception {
        Patch.Builder patch = new Patch.Builder();
        patch.test("/username", "trfields");
        System.out.println(patch.build().toString());
        JsonNode n = map.readTree(patch.build().toString());
        Assert.assertEquals(n.size(), 1);
    }
    
}
