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
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class PostmanTest {
    public static final String REST_SANDBOX = "/rest/sandbox/";
    public static final int TESTING_PORT = 8045;
    
    public ObjectMapper map;
    public RestCollection collection;
    
    public MockWebServer mws;
    
    @BeforeClass
    public void setUp() throws Exception {
        map = new ObjectMapper();
        collection = PostmanCollection.file(getClass().getResource(REST_SANDBOX + "PostmanTests.json.postman_collection"));
    }
    
    @BeforeMethod
    public void initTest() throws Exception {
        mws = new MockWebServer();
    }
    
    @AfterMethod
    public void cleanUpTest(ITestResult ires) throws Exception {
        mws.shutdown();
    }
    
    @Test
    public void testParameters() throws Exception {
        // GET /more/testing?q=v1&v=v2&a=v3 HTTP/1.1
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        collection.byName("Test Parameters").send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("GET /more/testing?q=v1&v=v2&a=v3 HTTP/1.1", rr.getRequestLine());
}
    
    @Test
    public void testParametersVariables() throws Exception {
        // GET /more/testing?q=v1&v=v2&a=v3 HTTP/1.1
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        Map env = new HashMap();
        env.put("var", "file:///var/variable");
        collection.byName("Test Variables").withEnv(env).send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("GET /more/testing?q=v1&v=v2&a=v3&x=file:///var/variable HTTP/1.1", rr.getRequestLine());
    }    
    
    @Test
    public void testUrlEncoding() throws Exception {
        // q=v1&v=v2&a=v3
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        collection.byName("Test Url Encoding").send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("q=v1&v=v2&a=v3", rr.getUtf8Body());
    }
    
    @Test
    public void testUrlEncodingVariables() throws Exception {
        // q=v1&v=v2&a=v3
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        Map env = new HashMap();
        env.put("var", "file:///var/variable");
        collection.byName("Test Url Encoding Variables").withEnv(env).send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("q=v1&v=v2&a=v3&x=" + URLEncoder.encode("file:///var/variable", "UTF-8"), rr.getUtf8Body());
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
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        collection.byName("Test Form Encoding").send();

        RecordedRequest rr = mws.takeRequest();

        Assert.assertTrue(rr.getUtf8Body().contains("v1"));
        Assert.assertTrue(rr.getUtf8Body().contains("v2"));
        Assert.assertTrue(rr.getUtf8Body().contains("v3"));
    }
    
    @Test
    public void testMultipartFormVariables() throws Exception {
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        Map env = new HashMap();
        env.put("var", "file:///var/variable");
        collection.byName("Test Form Variables").withEnv(env).send();

        RecordedRequest rr = mws.takeRequest();

        Assert.assertTrue(rr.getUtf8Body().contains("v1"));
        Assert.assertTrue(rr.getUtf8Body().contains("v2"));
        Assert.assertTrue(rr.getUtf8Body().contains("v3"));
        Assert.assertTrue(rr.getUtf8Body().contains("file:///var/variable"));   
    }
    
    
    @Test
    public void testRaw() throws Exception {
        // This is only a raw document, no formatting or special data type.
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        collection.byName("Test Raw Data").send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("This is only a raw document, no formatting or special data type.", rr.getUtf8Body());
    }
    
    @Test
    public void testRawVariables() throws Exception {
        // This is only a raw document, no formatting or special data type.
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        Map env = new HashMap();
        env.put("var", "file:///var/variable");
        collection.byName("Test Raw Variables").withEnv(env).send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("This is only a raw document, no formatting or special data type. It does, however, support file:///var/variable", rr.getUtf8Body());
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
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        collection.byName("Test Binary Data").send(new String(Files.readAllBytes(Paths.get(getClass().getResource(REST_SANDBOX).toURI()).resolve("schema.json"))));
        RecordedRequest rr = mws.takeRequest();
        Assert.assertTrue(rr.getUtf8Body().contains("patternProperties"));
    }
    
}
