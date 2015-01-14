/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.rest.Patch;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class PatchUtilityTest {
    
    private final ObjectMapper map;
    
    public PatchUtilityTest() {
        this.map = new ObjectMapper();
    }
    
    @Test
    public void buildPatchTest() throws Exception {
        Patch patch = new Patch.Builder().test("/username", "trfields").build();
        System.out.println(patch.toString());
        JsonNode n = map.readTree(patch.toString());
        Assert.assertEquals(n.size(), 1);
        
        AccountInformation m = new AccountInformation("trfields", "T. R. Fields");
        
        JsonNode toPatch = map.valueToTree(m);
        Assert.assertNotNull(patch.apply(toPatch));
    }
    
    @Test
    public void buildPatchReplace() throws Exception {
        Patch patch = new Patch.Builder().replace("/username", "trfields2").build();
        System.out.println(patch.toString());
        
        AccountInformation m = new AccountInformation("trfields", "T. R. Fields");
        
        JsonNode toPatch = map.valueToTree(m);
        Assert.assertNotNull(patch.apply(toPatch));
        Assert.assertEquals(toPatch.at("/username").asText(), "trfields2");
    }
    
    @Test
    public void buildPatchRemove() throws Exception {
        Patch patch = new Patch.Builder().remove("/username").build();
        System.out.println(patch.toString());
        
        AccountInformation m = new AccountInformation("trfields", "T. R. Fields");
        
        JsonNode toPatch = map.valueToTree(m);
        Assert.assertNotNull(patch.apply(toPatch));
        Assert.assertTrue(toPatch.at("/username").isMissingNode());
    }
    
    @Test
    public void buildPatchAdd() throws Exception {
        Patch patch = new Patch.Builder().add("/username2", "testing").build();
        System.out.println(patch.toString());
        
        AccountInformation m = new AccountInformation("trfields", "T. R. Fields");
        
        JsonNode toPatch = map.valueToTree(m);
        Assert.assertNotNull(patch.apply(toPatch));
        Assert.assertEquals(toPatch.at("/username2").asText(), "testing");
    }
    
    @Test
    public void buildPatchMove() throws Exception {
        Patch patch = new Patch.Builder().move("/username", "/username2").build();
        System.out.println(patch.toString());
        
        AccountInformation m = new AccountInformation("trfields", "T. R. Fields");
        
        JsonNode toPatch = map.valueToTree(m);
        Assert.assertNotNull(patch.apply(toPatch));
        Assert.assertEquals(toPatch.at("/username2").asText(), "trfields");
        Assert.assertTrue(toPatch.at("/username").isMissingNode());
    }
    
    @Test
    public void buildPatchCopy() throws Exception {
        Patch patch = new Patch.Builder().copy("/username", "/username2").build();
        System.out.println(patch.toString());
        
        AccountInformation m = new AccountInformation("trfields", "T. R. Fields");
        
        JsonNode toPatch = map.valueToTree(m);
        Assert.assertNotNull(patch.apply(toPatch));
        Assert.assertEquals(toPatch.at("/username2").asText(), "trfields");
        Assert.assertEquals(toPatch.at("/username2").asText(), "trfields");
    }
    
}
