/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest.blueprint;

import com.damnhandy.uri.template.UriTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.text.Template;
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
import com.orasi.utils.types.DefaultingMap;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        public RestResponse send() throws Exception {
            OkHttpClient client = new OkHttpClient();
            if(session() != null) {
                client.setCookieHandler(session().getCookieManager());
            }
            RequestFormat format = null;
            
            String body = null;
            Map variables = new DefaultingMap(env(), session().env());
            Headers.Builder hb = new Headers.Builder();
            for(JsonNode exa : action.path("examples")) {
                for(JsonNode req : exa.path("requests")) {
                    if(req.path("name").asText().equals(name)) {
                        body = req.path("body").asText();
                        for(JsonNode n : req.path("headers")) {
                            hb.add(Template.format(n.path("name").asText(), variables), Template.format(n.path("value").asText(), variables));
                        }
                    }
                }                
            }                   
                    
            String url = Template.format(this.url + resource.path("uriTemplate").asText(), variables);
            url = UriTemplate.fromTemplate(url).set(params()).expand();

            Request request = new RestRequestBuilder()
                    .format(RequestFormat.RAW)
                    .method(RequestType.valueOf(action.path("method").asText()))
                    .files(files())
                    .data(new LinkedList<RequestData>(), Template.format(body, variables))
                    .url(url)
                    .headers(hb.build()).build();

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
            try {
                System.out.println(Yaml.Map.writerWithDefaultPrettyPrinter().writeValueAsString(data));
            } catch (JsonProcessingException ex) {
                Logger.getLogger(SnowcrashCollection.class.getName()).log(Level.SEVERE, null, ex);
            }        
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
