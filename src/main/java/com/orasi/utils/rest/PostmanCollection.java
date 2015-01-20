/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okio.Okio;

/**
 * The PostmanCollection allows one to load a Postman Collection file, which
 * is just a specially formatted JSON file. It describes a user-selected group
 * of queries which have been saved in Postman.
 * 
 * @author Brian Becker
 */
public class PostmanCollection implements RestCollection {

    @Override
    public RestRequest byId(String id) {
        return ids.get(id);
    }

    @Override
    public RestRequest byName(String name) {
        return names.get(name);
    }
        
    public static class PostmanRequest implements RestRequest {        
        public static final MessageFormat fmt = new MessageFormat(
                "-- ID: {0} URL: {2} Method: {5} Name: {8} --");
        
        public String id;
        public String headers;
        public String url;
        public Map pathVariables;
        public String preRequestScript;
        public RequestType method;
        public List<RequestData> data;
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
        
        @Override
        public Response send(String... parameters) throws Exception {
            OkHttpClient client = new OkHttpClient();
            RequestFormat format = null;
            switch(dataMode) {
                case "params": format = RequestFormat.MULTIPART_FORM; break;
                case "urlencoded": format = RequestFormat.URLENCODE; break;
                case "binary": format = RequestFormat.RAW; break;
                case "raw": format = RequestFormat.RAW; break;
            }
                    
            Request request = RestRequestHelpers.request(method, headers, url, format, data, rawModeData, parameters);
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
    private final Map<String, RestRequest> ids;
    private final Map<String, RestRequest> names;
    
    public PostmanCollection(byte[] data) throws IOException {
        ObjectMapper map = new ObjectMapper();
        object = map.readValue(data, PostmanCollectionData.class);
        ids = new HashMap<String, RestRequest>();
        names = new HashMap<String, RestRequest>();
        for(PostmanRequest req : object.requests) {
            ids.put(req.id, req);
            names.put(req.name, req);
        }
    }
    
    public static RestCollection file(URL collection) throws Exception {
        return new PostmanCollection(Okio.buffer(Okio.source((InputStream)collection.getContent())).readByteArray());
    }
    
}
