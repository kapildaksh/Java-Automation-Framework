/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest.blueprint;

import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.text.Template;
import com.orasi.utils.rest.BaseExpectedNode;
import com.orasi.utils.rest.ExpectedResponse;
import com.orasi.utils.rest.NullRestRequest;
import com.orasi.utils.rest.ResponseVerifier;
import com.orasi.utils.rest.RestCollection;
import com.orasi.utils.rest.RestRequest;
import com.orasi.utils.rest.RestSession;
import com.orasi.utils.rest.Yaml;
import com.orasi.utils.types.DefaultingMap;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
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
            Map variables = new DefaultingMap(env(), session().env());            
            boolean found = false;
            for(JsonNode exa : action.path("examples")) {
                for(JsonNode req : exa.path("requests")) {
                    if(req.path("name").asText().equals(this.name)) {
                        found = true;
                    }
                }
                if(exa.path("requests").asText().equals("")) { found = true; }
                if(found) {
                    for(JsonNode res : exa.path("responses")) {
                        if(res.path("name").asText().equals(name)) {
                            return new ResponseVerifier(this, Template.format(res.path("body").asText(), variables), node);
                        }
                    }
                }
                found = false;                
            }   
            throw new RuntimeException("Response named '" + name + "' not found.");
        }

        @Override
        public Response send() throws Exception {
            
            String body = null;
            String mediatype = null;
            Map variables = new DefaultingMap(env(), session().env());
            MultivaluedMap headers = new MultivaluedHashMap();
            for(JsonNode exa : action.path("examples")) {
                for(JsonNode req : exa.path("requests")) {
                    if(req.path("name").asText().equals(name)) {
                        body = req.path("body").asText();
                        for(JsonNode n : req.path("headers")) {
                            headers.add(Template.format(n.path("name").asText(), variables), Template.format(n.path("value").asText(), variables));
                            if(n.path("name").asText().equals("Content-Type")) {
                                mediatype = n.path("value").asText();
                            }
                        }
                    }
                }                
            }                 
            
            if(mediatype == null) mediatype = "text/plain; charset=utf-8";
            if(body == null) body = "";
                    
            Client client = ClientBuilder.newClient().register(session());
            Entity payload = Entity.entity(Template.format(body, variables), mediatype);
            
            Response response = client.target(url)
                    .path(resource.path("uriTemplate").asText())
                    .resolveTemplates(params())
                    .request()
                    .headers(headers)
                    .method(action.path("method").asText(), payload);
            
            return response;
        }
    }
    
    private final JsonNode data;
    private final RestSession session;
    private final String url;
    
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
                                    return new SnowcrashRequest(this.url, res, act, names.get(2)).session(session());
                                } else {
                                    return new SnowcrashRequest(this.url, res, act, "").session(session());
                                }
                            }
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("Name parsing exception: Expects \"Resource Name: Action Name\" or \"Resource Name: Action Name: #\"");
        }
               
        return new NullRestRequest();
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
