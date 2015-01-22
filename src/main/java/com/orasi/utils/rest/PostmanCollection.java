/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.orasi.utils.types.DefaultingMap;
import com.orasi.utils.types.Reference;
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
import org.apache.commons.lang3.StringUtils;

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
    
    public static class ResponseCode {
        public Number code;
        public String name;
        public String detail;
    }
    
    public static class Header {
        public String name;
        public String key;
        public String value;
        public String description;
    }
    
    public static class State {
        public String size;
    }
    
    public static class PostmanResponseRequest {
        public String url;
        public Map pathVariables;
        public JsonNode data;
        public String headers;
        public String dataMode;
        public String method;
        public String tests;
        public Number version;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SampleResponse extends ExpectedResponse {

        @Override
        public String expected() {
            return this.text;
        }
        
        @Override
        public String returned() {
            return this.realResponseText;
        }
                        
        public String status;
        public ResponseCode responseCode;
        public Number time;
        public List<Header> headers;
        public List<String> cookies;
        public String mime;
        public String text;
        public String language;
        public String rawDataType;
        public State state;
        public String previewType;
        public Number searchResultScrolledTo;
        public Boolean forceRaw;
        public String id;
        public String name;
        public PostmanResponseRequest request;
        
        @JsonIgnore
        public Response realResponse;
        @JsonIgnore
        public String realResponseText;
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
        public List<SampleResponse> responses;
        public String tests;
        public String currentHelper;
        public Map helperAttributes;
        public String collectionId;
        public Boolean synced;
        public String rawModeData;
        
        @JsonIgnore
        public Map requestVariables;
        @JsonIgnore
        public Map requestDefaultVariables;
        @JsonIgnore
        public String[] files;
        
        @Override
        public RestRequest env(Map vars) {
            this.requestVariables = vars;
            return this;
        }
        
        @Override
        public String toString() {
            return fmt.format(new Object[] { id, headers, url, pathVariables, 
                    preRequestScript, method, data, dataMode, name, description,
                    descriptionFormat, time, version, responses, tests, 
                    currentHelper, helperAttributes, collectionId, synced, 
                    rawModeData });
        }
        
        @Override
        public RestRequest params(String... parts) {
            String[] temp = StringUtils.substringsBetween(url, "/:", "/");
            int i = 0;
            if(temp != null && temp.length > 0) {
                for(String t : temp) {
                     System.out.println(t);
                     url = url.replace("/:" + t + "/", "/" + parts[i++] + "/");
                }
            }
            System.out.println(url);
            return this;
        }
        
        @Override
        public RestRequest files(String... files) {
            this.files = files;
            return this;
        }
        
        @Override
        public Response send() throws Exception {
            OkHttpClient client = new OkHttpClient();
            RequestFormat format = null;
            switch(dataMode) {
                case "params": format = RequestFormat.MULTIPART_FORM; break;
                case "urlencoded": format = RequestFormat.URLENCODE; break;
                case "binary": format = RequestFormat.RAW; break;
                case "raw": format = RequestFormat.RAW; break;
            }
            
            Map variables = new DefaultingMap(requestVariables, requestDefaultVariables, new Function<String, String>() {
                @Override
                public String apply(String f) {
                    return "{{" + f + "}}";
                }
            });
            
            String safeUrl, safeRawModeData;
            
            safeUrl = RestRequestHelpers.variables(url, variables);
            safeRawModeData = RestRequestHelpers.variables(rawModeData, variables);
            
            // CRITICAL
            if(data != null) {
                RestRequestHelpers.variables(data, variables);
            }
            
            Request request = RestRequestHelpers.request(method, headers, safeUrl, format, data, safeRawModeData, files);
            Response response = client.newCall(request).execute();
            
            return response;
        }
        
        @Override
        public ExpectedResponse response(String name) {
            try {
                for(SampleResponse r : this.responses) {
                    System.out.println(name);
                    if(r.name.equals(name)) {
                        r.realResponse = this.send();
                        r.realResponseText = r.realResponse.body().string();
                        return r;
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            }
            return new SampleResponse();
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
    private final Map defaultVariables;
    
    public PostmanCollection(byte[] data) throws IOException {
        ObjectMapper map = new ObjectMapper();
        object = map.readValue(data, PostmanCollectionData.class);
        ids = new HashMap<String, RestRequest>();
        names = new HashMap<String, RestRequest>();
        defaultVariables = new HashMap();
        for(PostmanRequest req : object.requests) {
            req.requestDefaultVariables = defaultVariables;
            ids.put(req.id, req);
            names.put(req.name, req);
        }
    }
    
    @Override
    public RestCollection env(Map variables) {
        defaultVariables.putAll(variables);
        return this;
    }
    
    public static RestCollection file(URL collection) throws Exception {
        return new PostmanCollection(Okio.buffer(Okio.source((InputStream)collection.getContent())).readByteArray());
    }
        
}
