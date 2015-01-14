/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.rest.Patch;
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
                new ServiceResponse(ServiceResponse.Type.SUCCESS, 200)
        )));
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(
                new AccountInformation("trfields", "T. R. Fields")
        )));
        server.play();
        URL url = server.getUrl("/users/trfields");
        
        RequestBody body = RequestBody.create(JSON, map.writeValueAsString(
                new AccountInformation("trfields", "T. R. Fields")
        ));
        Request request1 = new Request.Builder().url(url).put(body).build();
        Response response1 = client.newCall(request1).execute();
        
        Request request2 = new Request.Builder().url(url).get().build();
        Response response2 = client.newCall(request2).execute();
        
        RecordedRequest recorded1 = server.takeRequest();
        Assert.assertEquals(map.readTree(recorded1.getUtf8Body()), map.readTree(response2.body().string()));
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
                new ServiceResponse(ServiceResponse.Type.SUCCESS, 200)
        )));
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(
                new ServiceResponse(ServiceResponse.Type.ERROR, 404)
        )).setStatus("HTTP/1.1 404 Resource not found"));
        
        server.play();
        URL url = server.getUrl("/users/trfields");
        
        Request request1 = new Request.Builder().url(url).delete().build();
        Response response1 = client.newCall(request1).execute();
        
        Request request2 = new Request.Builder().url(url).get().build();
        Response response2 = client.newCall(request2).execute();
        JsonNode body = map.readTree(response2.body().string());
        System.out.println(body.toString());
        
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
                new ServiceResponse(ServiceResponse.Type.SUCCESS, 200)
        )));
        server.enqueue(new MockResponse().setHeader("Content-Type", JSON).setBody(map.writeValueAsString(new AccountInformation("trfields", "Tom"))));        
        server.play();
        URL url = server.getUrl("/users/trfields");
        
        Patch.Builder patch = new Patch.Builder();
        patch.test("/username", "trfields");
        patch.replace("/nickname", "Tom");
        RequestBody patchBody = RequestBody.create(JSON_PATCH, patch.toString());
        Request request1 = new Request.Builder().url(url).patch(patchBody).build();
        Response response1 = client.newCall(request1).execute();
        ServiceResponse body1 = map.readValue(response1.body().string(), ServiceResponse.class);
        
        Assert.assertEquals(body1.code, 200);
        Assert.assertEquals(body1.type, ServiceResponse.Type.SUCCESS);
        
        Request request2 = new Request.Builder().url(url).get().build();
        Response response2 = client.newCall(request2).execute();
        AccountInformation body2 = map.readValue(response2.body().string(), AccountInformation.class);
        
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
