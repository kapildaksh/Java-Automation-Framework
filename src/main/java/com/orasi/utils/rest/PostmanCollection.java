/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.orasi.utils.types.DefaultingMap;
import com.orasi.utils.types.Reference;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import edu.emory.mathcs.backport.java.util.Arrays;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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
        
        @JsonIgnore
        public Map requestVariables;
        @JsonIgnore
        public Reference<Map> requestDefaultVariables;
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
            
            Map variables = new DefaultingMap(requestVariables, requestDefaultVariables.get(), new Function<String, String>() {
                @Override
                public String apply(String f) {
                    return "{{" + f + "}}";
                }
            });
            
            url = RestRequestHelpers.variables(url, variables);
            rawModeData = RestRequestHelpers.variables(rawModeData, variables);
            
            if(data != null) {
                RestRequestHelpers.variables(data, variables);
            }
            
            Request request = RestRequestHelpers.request(method, headers, url, format, data, rawModeData, files);
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
    private final Reference<Map> defaultVariables;
    
    public PostmanCollection(byte[] data) throws IOException {
        ObjectMapper map = new ObjectMapper();
        object = map.readValue(data, PostmanCollectionData.class);
        ids = new HashMap<String, RestRequest>();
        names = new HashMap<String, RestRequest>();
        defaultVariables = new Reference<Map>(new HashMap());
        for(PostmanRequest req : object.requests) {
            req.requestDefaultVariables = defaultVariables;
            ids.put(req.id, req);
            names.put(req.name, req);
        }
    }
    
    @Override
    public RestCollection env(Map variables) {
        defaultVariables.set(variables);
        return this;
    }
    
    public static RestCollection file(URL collection) throws Exception {
        return new PostmanCollection(Okio.buffer(Okio.source((InputStream)collection.getContent())).readByteArray());
    }
        
}
