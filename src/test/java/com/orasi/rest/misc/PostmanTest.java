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
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test the Postman RestCollection class, which takes an input description
 * file and allows calling any of the individual web services conveniently
 * by the name you assigned to them in Postman. Here we ensure the various
 * different types of output are functioning properly.
 * 
 * NOTE: REST is typically considered to include JSON and XML web services.
 * You will find multipart form encoding, url encoding, and a variety of
 * file upload options in Postman. While not typically considered within
 * the scope of REST, this is a generic HTTP client and providing all of the
 * methods will allow mostly-REST services to be tested fully.
 * 
 * @author Brian Becker
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
        
        collection.get("Test Parameters").send();
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
        collection.get("Test Variables").env(env).send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("GET /more/testing?q=v1&v=v2&a=v3&x=file:///var/variable HTTP/1.1", rr.getRequestLine());
    }    
    
    @Test
    public void testParametersVariablesPathParameters() throws Exception {
        // GET /more/testing?q=v1&v=v2&a=v3 HTTP/1.1
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        Map env = new HashMap();
        env.put("var", "file:///var/variable");
        Map params = new HashMap();
        params.put("which", "more");
        collection.get("Url Parameters").env(env).params(params).send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertEquals("GET /more/testing?q=v1&v=v2&a=v3&x=file:///var/variable HTTP/1.1", rr.getRequestLine());
    }        
    
    @Test
    public void testUrlEncoding() throws Exception {
        // q=v1&v=v2&a=v3
        mws.enqueue(new MockResponse());       
        mws.play(TESTING_PORT);
        
        collection.get("Test Url Encoding").send();
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
        collection.get("Test Url Encoding Variables").env(env).send();
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
        
        collection.get("Test Form Encoding").send();

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
        collection.get("Test Form Variables").env(env).send();

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
        
        collection.get("Test Raw Data").send();
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
        collection.get("Test Raw Variables").env(env).send();
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
        
        collection.get("Test Binary Data").files(new String(Files.readAllBytes(Paths.get(getClass().getResource(REST_SANDBOX).toURI()).resolve("schema.json")))).send();
        RecordedRequest rr = mws.takeRequest();
        Assert.assertTrue(rr.getUtf8Body().contains("patternProperties"));
    }
    
}
