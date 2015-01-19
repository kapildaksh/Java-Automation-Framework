/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import okio.Okio;

/**
 *
 * @author brian.becker
 */
public class PostmanCollection {
    
    public static enum RequestType {
        GET, POST, PUT, PATCH, DELETE, COPY, HEAD, OPTIONS,
        LINK, UNLINK, PURGE, LOCK, UNLOCK, PROPFIND
    }
        
    public static class PostmanRequest {        
        public static final MessageFormat fmt = new MessageFormat(
                "ID: {0}\nHeaders: {1}\nURL: {2}\nVars: {3}\n" +
                "Script: {4}\nMethod: {5}\nData: {6}\nData Mode {7}\n" +
                "Name: {8}\nDescription: {9}\nDescription Format: {10}\n" +
                "Time: {11}\nVersion: {12}\nResponses: {13}\nTests: {14}\n" +
                "Helper: {15}\nHelper Attributes: {16}\nCollection: {17}\n" +
                "Synced: {18}\nRaw Mode Data: {19}");
        
        public String id;
        public String headers;
        public String url;
        public Map pathVariables;
        public String preRequestScript;
        public RequestType method;
        public List<String> data;
        public String dataMode;
        public String name;
        public String description;
        public String descriptionFormat;
        public Date time;
        public Number version;
        public List<String> responses;
        public String tests;
        public String currentHelper;
        public Map helperAttributes;
        public String collectionId;
        public Boolean synced;
        public String rawModeData;
        
        @Override
        public String toString() {
            return fmt.format(new Object[] { id, headers, url, pathVariables, 
                    preRequestScript, method, data, dataMode, name, description,
                    descriptionFormat, time, version, responses, tests, 
                    currentHelper, helperAttributes, collectionId, synced, 
                    rawModeData });
        }
        
        public Response send() throws IOException {
            Headers.Builder hdrs = new Headers.Builder();
            for(String hdr : headers.split(Pattern.quote("\n"))) {
                String[] h = hdr.split(Pattern.quote(":"), 2);
                hdrs = hdrs.add(h[0], h[1]);
            }
            
            RequestBody body;
            if(rawModeData == null) {
                body = null;
            } else {
                body = RequestBody.create(null, rawModeData);
            }
            
            Request request = new Request.Builder()
                    .url(url)
                    .headers(hdrs.build())
                    .method(method.toString(), body).build();
            
            OkHttpClient client = new OkHttpClient();
            
            Response response = client.newCall(request).execute();
            
            return response;
        }
    }
    
    public static class PostmanCollectionData {        
        public String id;
        public String name;
        public String description;
        public List<String> order;
        public List<String> folders;
        public Date timestamp;
        public Boolean synced;
        public String remoteLink;
        public List<PostmanRequest> requests;
    }
    
    private final PostmanCollectionData object;
    
    public PostmanCollection(byte[] data) throws IOException {
        ObjectMapper map = new ObjectMapper();
        object = map.readValue(data, PostmanCollectionData.class);
    }
    
    public static PostmanCollection fromPath(Path collection) throws IOException {
        return new PostmanCollection(Okio.buffer(Okio.source(collection)).readByteArray());
    }
    
    public static PostmanCollection fromWeb(URL collection) throws IOException {
        return new PostmanCollection(Okio.buffer(Okio.source(collection.openStream())).readByteArray());
    }
    
    public PostmanRequest getRequestByName(String name) {
        for(PostmanRequest request : object.requests) {
            if(request.name.equals(name))
                return request;
        }
        return new PostmanRequest();        
    }
    
    public PostmanRequest getRequestById(String id) {
        for(PostmanRequest request : object.requests) {
            if(request.id.equals(id))
                return request;
        }
        return new PostmanRequest();                
    }
    
    public Collection<PostmanRequest> getRequestsInOrder() {
        List<PostmanRequest> l = new LinkedList<PostmanRequest>();
        for(String reqs : object.order) {
            l.add(getRequestById(reqs));
        }
        return l;
    }

}
