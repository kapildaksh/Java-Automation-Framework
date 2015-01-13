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
        server.play();
        URL url = server.getUrl("/users/trfields");
        
        RequestBody body = RequestBody.create(JSON, "{ }");
        Request request = new Request.Builder().url(url).put(body).build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        
        RecordedRequest request1 = server.takeRequest();
        Assert.assertEquals(request1.getPath(), "/users/trfields");
        Assert.assertEquals(request1.getMethod(), "PUT");
        server.shutdown();
    }
    
    @Test
    public void deleteAccount() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(
                new BasicMessage(200, RequestType.DELETE, "Deleted user successfully")
        )));
        server.play();
        URL url = server.getUrl("/users/trfields");
        
        Request request = new Request.Builder().url(url).delete().build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        
        RecordedRequest request1 = server.takeRequest();
        Assert.assertEquals(request1.getPath(), "/users/trfields");
        Assert.assertEquals(request1.getMethod(), "DELETE");
        server.shutdown();
    }
    
}
