/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.rest.PostmanCollection;
import com.orasi.utils.rest.RestCollection;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import junit.framework.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class PostmanTest {
    public static final String REST_SANDBOX = "/rest/sandbox/";
    
    public ObjectMapper map;
    public RestCollection collection;
       
    @BeforeClass
    public void setUp() throws Exception {
        map = new ObjectMapper();
        collection = PostmanCollection.file(getClass().getResource(REST_SANDBOX + "PostmanTests.json.postman_collection"));
    }
    
    @Test
    public void testParameters() throws Exception {
        // GET /more/testing?q=v1&v=v2&a=v3 HTTP/1.1
        MockWebServer mws = new MockWebServer();
        mws.enqueue(new MockResponse());       
        mws.play(8045);
        
        collection.byName("Test Parameters").send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("GET /more/testing?q=v1&v=v2&a=v3 HTTP/1.1", rr.getRequestLine());
        
        mws.shutdown();
    }
    
    @Test
    public void testUrlEncoding() throws Exception {
        // q=v1&v=v2&a=v3
        MockWebServer mws = new MockWebServer();
        mws.enqueue(new MockResponse());       
        mws.play(8045);
        
        collection.byName("Test Url Encoding").send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("q=v1&v=v2&a=v3", rr.getUtf8Body());
        
        mws.shutdown();
    }
    
    @Test
    public void testMultipartForm() throws Exception {
        // ------WebKitFormBoundaryeMvs55AiRqYQEjHV
        // Content-Disposition: form-data; name="q"

        // v1
        // ------WebKitFormBoundaryeMvs55AiRqYQEjHV
        // Content-Disposition: form-data; name="v"

        // v2
        // ------WebKitFormBoundaryeMvs55AiRqYQEjHV
        // Content-Disposition: form-data; name="a"

        // v3
        // ------WebKitFormBoundaryeMvs55AiRqYQEjHV--
        MockWebServer mws = new MockWebServer();
        mws.enqueue(new MockResponse());       
        mws.play(8045);
        
        collection.byName("Test Form Encoding").send();

        RecordedRequest rr = mws.takeRequest();

        Assert.assertTrue(rr.getUtf8Body().contains("v1"));
        Assert.assertTrue(rr.getUtf8Body().contains("v2"));
        Assert.assertTrue(rr.getUtf8Body().contains("v3"));
        
        mws.shutdown();        
    }
    
    @Test
    public void testRaw() throws Exception {
        // This is only a raw document, no formatting or special data type.
        MockWebServer mws = new MockWebServer();
        mws.enqueue(new MockResponse());       
        mws.play(8045);
        
        collection.byName("Test Raw Data").send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("This is only a raw document, no formatting or special data type.", rr.getUtf8Body());
        
        mws.shutdown();        
    }
    
    @Test
    public void testBinary() throws Exception {
        // {
        //      "$schema": "file:///C:/Users/brian.becker/Git/java-rest-schema/target/test-classes/schema.json",
        //      "type": "object",
        //      "properties": {
        //          "/": { "$ref": "file:///C:/Users/brian.becker/Git/java-rest-schema/target/test-classes/entry-schema.json" }
        //      },
        //      "patternProperties": {
                    //"^(/[^/]+)+$": { "$ref": "file:///C:/Users/brian.becker/Git/java-rest-schema/target/test-classes/entry-schema.json" }
        //      },
        //      "additionalProperties": false,
        //      "required": [ "/" ]
        // }
        
        MockWebServer mws = new MockWebServer();
        mws.enqueue(new MockResponse());       
        mws.play(8045);
        
        collection.byName("Test Binary Data").send(new String(Files.readAllBytes(Paths.get(getClass().getResource(REST_SANDBOX).toURI()).resolve("schema.json"))));
        RecordedRequest rr = mws.takeRequest();
        Assert.assertTrue(rr.getUtf8Body().contains("patternProperties"));
        
        mws.shutdown();
    }
    
}
