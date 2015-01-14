/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest;

import com.github.arven.rest.api.BasicMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.arven.rest.api.AccountInformationMessage;
import com.github.arven.rest.api.BasicMessage.RequestType;
import com.github.arven.rest.util.PatchBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class AccountServiceTests {
    
    public static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");
    
    public static final MediaType JSON_PATCH
            = MediaType.parse("application/json-patch+json; charset=utf-8");
    
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
        Assert.assertFalse(body.getSuccessful());
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
    
    @Test
    public void updateAccount() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(
                new BasicMessage(200, RequestType.UPDATE, "Modified user data successfully")
        )));
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(
                new AccountInformationMessage(
                        new BasicMessage(200, RequestType.READ, "Read user data successfully"),
                        "trfields",
                        "Tom"))));        
        server.play();
        URL url = server.getUrl("/users/trfields");
        
        PatchBuilder patch = new PatchBuilder();
        patch.test("/username", "trfields");
        patch.replace("/nickname", "Tom");
        RequestBody patchBody = RequestBody.create(JSON_PATCH, patch.toString());
        Request request1 = new Request.Builder().url(url).patch(patchBody).build();
        Response response1 = client.newCall(request1).execute();
        BasicMessage body1 = map.readValue(response1.body().string(), BasicMessage.class);
        
        Assert.assertEquals(body1.getErrorCode(), 200);
        Assert.assertEquals(body1.getMessage(), "Modified user data successfully");
        Assert.assertEquals(body1.getType(), RequestType.UPDATE);
        Assert.assertTrue(body1.getSuccessful());
        
        Request request2 = new Request.Builder().url(url).get().build();
        Response response2 = client.newCall(request2).execute();
        AccountInformationMessage body2 = map.readValue(response2.body().string(), AccountInformationMessage.class);
        
        Assert.assertEquals(body2.nickname, "Tom");
        Assert.assertEquals(body2.username, "trfields");
        
        RecordedRequest recorded1 = server.takeRequest();
        Assert.assertEquals(recorded1.getPath(), "/users/trfields");
        Assert.assertEquals(recorded1.getMethod(), "PATCH");
        
        RecordedRequest recorded2 = server.takeRequest();
        Assert.assertEquals(recorded2.getPath(), "/users/trfields");
        Assert.assertEquals(recorded2.getMethod(), "GET");
        server.shutdown();
    }
    
}
