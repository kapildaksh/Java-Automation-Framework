/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.arven.sandbox.MockMicroblogServer;
import com.orasi.utils.rest.PostmanCollection;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class PostmanTests {
    public static final String REST_SANDBOX = "/rest/sandbox/";
    
    public ObjectMapper map;
    public PostmanCollection collection;
    public MockMicroblogServer server;
    
    @BeforeClass
    public void setUp() throws IOException, URISyntaxException {
        map = new ObjectMapper();
        collection = PostmanCollection.fromPath(Paths.get(getClass().getResource(REST_SANDBOX).toURI()).resolve("PostmanTests.json.postman_collection"));
        server = new MockMicroblogServer();
        Thread t = new Thread(server);
        t.start();
    }
    
    @AfterClass
    public void tearDown() {
        server.stop();
    }
    
    @Test
    public void testParameters() {
        
    }
    
    @Test
    public void testUrlEncoding() {
        
    }
    
    @Test
    public void testMultipartForm() {
        
    }
    
    @Test
    public void testRaw() {
        
    }
    
    @Test
    public void testBinary() {
        
    }
    
    @Test
    public void testFillInFiles() {
        
    }
    
}
