/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest;

import com.github.arven.rest.api.BasicMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arven.rest.api.BasicMessage.RequestType;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import java.io.IOException;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class AccountServiceTests {
    
    public static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
    
    private MockWebServer server;
    private final OkHttpClient client;
    private final ObjectMapper map;
   
    public AccountServiceTests() {
        client = new OkHttpClient();
        map = new ObjectMapper(); 
    }
    
    @Test
    public void registerAccount() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(
                new BasicMessage(200, RequestType.CREATE, "Created user successfully")
        )));
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(
                new BasicMessage(200, RequestType.READ, "Read user data successfully")
        )));
        server.play();
        URL url = server.getUrl("/users/trfields");
        
        RequestBody body = RequestBody.create(JSON, "{ }");
        Request request1 = new Request.Builder().url(url).put(body).build();
        Response response1 = client.newCall(request1).execute();
        System.out.println(response1.body().string());
        
        Request request2 = new Request.Builder().url(url).get().build();
        Response response2 = client.newCall(request2).execute();
        System.out.println(response2.body().string());        
        
        RecordedRequest recorded1 = server.takeRequest();
        Assert.assertEquals(recorded1.getPath(), "/users/trfields");
        Assert.assertEquals(recorded1.getMethod(), "PUT");
        
        RecordedRequest recorded2 = server.takeRequest();
        Assert.assertEquals(recorded2.getPath(), "/users/trfields");
        Assert.assertEquals(recorded2.getMethod(), "GET");
        server.shutdown();
    }
    
    @Test
    public void deleteAccount() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(
                new BasicMessage(200, RequestType.DELETE, "Deleted user successfully")
        )));
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(
                new BasicMessage(404, RequestType.READ, "User not found")
        )).setStatus("HTTP/1.1 404 Resource not found"));
        
        server.play();
        URL url = server.getUrl("/users/trfields");
        
        Request request1 = new Request.Builder().url(url).delete().build();
        Response response1 = client.newCall(request1).execute();
        
        Request request2 = new Request.Builder().url(url).get().build();
        Response response2 = client.newCall(request2).execute();
        BasicMessage body = map.readValue(response2.body().string(), BasicMessage.class);
        Assert.assertEquals(response2.code(), 404);
        Assert.assertEquals(body.getErrorCode(), 404);
        Assert.assertEquals(body.getSuccessful(), false);
        Assert.assertEquals(body.getMessage(), "User not found");
        Assert.assertEquals(body.getType(), RequestType.READ);
        
        RecordedRequest recorded1 = server.takeRequest();
        Assert.assertEquals(recorded1.getPath(), "/users/trfields");
        Assert.assertEquals(recorded1.getMethod(), "DELETE");
        
        RecordedRequest recorded2 = server.takeRequest();
        Assert.assertEquals(recorded2.getPath(), "/users/trfields");
        Assert.assertEquals(recorded2.getMethod(), "GET");
        server.shutdown();
    }
    
}
