/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest.blueprint;

import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.utils.rest.BaseExpectedNode;
import com.orasi.utils.rest.ExpectedResponse;
import com.orasi.utils.rest.OkRestResponse;
import com.orasi.utils.rest.ResponseVerifier;
import com.orasi.utils.rest.RestCollection;
import com.orasi.utils.rest.RestRequest;
import com.orasi.utils.rest.RestRequestBuilder;
import com.orasi.utils.rest.RestResponse;
import com.orasi.utils.rest.RestSession;
import com.orasi.utils.rest.Yaml;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 *
 * @author Brian Becker
 */
public class SnowcrashCollection implements RestCollection {
    
    private static class SnowcrashRequest extends RestRequest {
        private final JsonNode resource;
        private final String url;
        private JsonNode action = null;
        private String name = null;
        
        private SnowcrashRequest(String url, JsonNode resource, JsonNode action, String name) {
            this.action = action;
            this.name = name;
            this.resource = resource;
            this.url = url;
        }

        @Override
        public ExpectedResponse response(String name, BaseExpectedNode node) {
            for(JsonNode r : action.path("examples").path(0).path("responses")) {
                return new ResponseVerifier(this, r.path("body").asText(), node);
            }
            throw new RuntimeException("Response named '" + name + "' not found.");
        }

        @Override
        public RestResponse send() throws Exception {
            OkHttpClient client = new OkHttpClient();
            if(session() != null) {
                client.setCookieHandler(session().getCookieManager());
            }
            RequestFormat format = null;
            
            String body = null;
            for(JsonNode req : action.path("examples").get(0).path("requests")) {
                if(req.path("name").asText().equals(name)) {
                    body = req.path("body").asText();
                }
            }
            
            Request request = new RestRequestBuilder()
                    .format(RequestFormat.RAW)
                    .method(RequestType.valueOf(action.path("method").asText()))
                    .variables(new HashMap())
                    .params(params())
                    .files(files())
                    .data(new LinkedList<RequestData>(), body)
                    .auth(new HashMap())
                    .url(this.url + resource.path("uriTemplate").asText())
                    .headers("").build();            
            return new OkRestResponse(client.newCall(request).execute());
        }
    }
    
    private final JsonNode data;
    private final RestSession session;
    private String url;
    
    private SnowcrashCollection(JsonNode data, String endpoint) {
        this.session = new RestSession();
        this.data = data;
        this.url = endpoint;
    }
    
    /**
     * Get a Blueprint Rest Request by name.
     * 
     * @param name
     * @return 
     */
    @Override
    public RestRequest get(String name) {
        List<String> names = Arrays.asList(name.split(": "));
        if(names.size() >= 2 && names.size() <= 3) {
            String resname = names.get(0);
            String actname = names.get(1);
            for(JsonNode rg : data.path("resourceGroups")) {
                for(JsonNode res : rg.path("resources")) {
                    if(res.path("name").asText().equals(resname)) {
                        for(JsonNode act : res.path("actions")) {
                            if(act.path("name").asText().equals(actname)) {
                                if(names.size() == 3) {
                                    return new SnowcrashRequest(this.url, res, act, names.get(2));
                                } else {
                                    return new SnowcrashRequest(this.url, res, act, "");
                                }
                            }
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("Name parsing exception: Expects \"Resource Name: Action Name\" or \"Resource Name: Action Name: #\"");
        }
               
        return new RestRequest() {
            @Override
            public ExpectedResponse response(String name, BaseExpectedNode node) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public RestResponse send() throws Exception {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    @Override
    public RestSession session() {
        return this.session;
    }
    
    /**
     * Create a Collection from a file. This takes in a JSON file and returns
     * a class of the BlueprintCollection type.
     * 
     * @param collection
     * @param endpoint
     * @return 
     */
    public static RestCollection file(URL collection, String endpoint) {
        try {
            Process p = Runtime.getRuntime().exec("./bin/snowcrash");
            BufferedSource in = Okio.buffer(Okio.source(p.getInputStream()));
            BufferedSink out = Okio.buffer(Okio.sink(p.getOutputStream()));
            BufferedSource data = Okio.buffer(Okio.source((InputStream)collection.getContent()));
            out.writeAll(data);
            out.close();            
            return new SnowcrashCollection(Yaml.Map.readTree(in.inputStream()), endpoint);
        } catch (Exception ex) {
            throw new RuntimeException("Error loading collection from Snowcrash file", ex);
        }
    }
    
}
