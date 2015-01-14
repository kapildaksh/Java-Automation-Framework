/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arven.rest.api.AccountInformationMessage;
import com.github.arven.rest.api.BasicMessage;
import com.github.arven.rest.api.BasicMessage.RequestType;
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
        Patch patch = new Patch.Builder().test("/username", "trfields").build();
        System.out.println(patch.toString());
        JsonNode n = map.readTree(patch.toString());
        Assert.assertEquals(n.size(), 1);
        
        AccountInformationMessage m = new AccountInformationMessage(
                new BasicMessage(200, RequestType.READ, "Read user successfully"),
                "trfields", "T. R. Fields");
    }
    
}
