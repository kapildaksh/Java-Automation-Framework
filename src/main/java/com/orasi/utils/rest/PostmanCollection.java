package com.orasi.utils.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.orasi.utils.types.DefaultingMap;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okio.Okio;

/**
 * The PostmanCollection allows one to load a Postman Collection file, which
 * is just a specially formatted JSON file. It describes a user-selected group
 * of queries which have been saved in Postman. The collection contains a group
 * of requests, which also potentially have a number of responses. These
 * responses were generated by making a request in Postman and then clicking
 * the bookmark icon in Postman.
 * 
 * @author Brian Becker
 */
public class PostmanCollection implements RestCollection {

    /**
     * Get a Postman Rest Request by name.
     * 
     * @param name
     * @return 
     */
    @Override
    public RestRequest get(String name) {
        if(names.containsKey(name)) {
            return names.get(name);
        } else {
            throw new RuntimeException("Request named '" + name + "' not found.");
        }
    }
    
    /**
     * The SampleResponseData consists of the data values retrieved from the
     * request. It represents an actual request which has been generated in an
     * exploratory test.
     */
    public static class SampleResponseData {
        private String text;
        private String name;
    }
    
    /**
     * The Postman Request Data is the bulk of the data which is imported by
     * the PostmanCollection class.
     */
    private static class PostmanRequestData {
        private String id;
        private String headers;
        private String url;
        private RestRequest.RequestType method;
        private List<RestRequest.RequestData> data;
        private String dataMode;
        private String name;
        private List<SampleResponseData> responses;
        private Map helperAttributes;
        private String rawModeData;
    }
    
    /**
     * A PostmanRequest is a type of RestRequest which is based on Postman
     * requests stored in a collection file.
     */
    public static class PostmanRequest extends RestRequest {        
        private static final MessageFormat fmt = new MessageFormat(
                "-- ID: {0} URL: {2} Method: {5} Name: {8} --");
        
        private final PostmanRequestData data;
        
        /**
         * Create a PostmanRequest with the required data.
         * 
         * @param data 
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)        
        private PostmanRequest(PostmanRequestData data) {
            this.data = data;
        }
                
        @Override
        public String toString() {
            return fmt.format(new Object[] { data.id, data.url, data.method, data.name });
        }
               
        /**
         * Send this request, and get an OkHttpClient response back which
         * can be evaluated. This does no checking, only providing a quick
         * way to fire off REST requests based on a transcript.
         * 
         * @return
         * @throws Exception 
         */
        @Override
        public RestResponse send() throws Exception {
            OkHttpClient client = new OkHttpClient();
            if(session() != null) {
                client.setCookieHandler(session().getCookieManager());
            }
            RequestFormat format = null;
            switch(data.dataMode) {
                case "params": format = RequestFormat.MULTIPART_FORM; break;
                case "urlencoded": format = RequestFormat.URLENCODE; break;
                case "binary": format = RequestFormat.RAW; break;
                case "raw": format = RequestFormat.RAW; break;
            }
            
            Map variables = new DefaultingMap(env(), session().env());
            Request request = RestRequestHelpers.request(data.method, data.headers, applyParams(data.url), data.helperAttributes, format, data.data, data.rawModeData, variables, files());
            RestResponse response = new OkRestResponse(client.newCall(request).execute());
                        
            return response;
        }
        
        /**
         * This returns an expected response, which is a predetermined response
         * which was returned by the server for this given request in the past.
         *
         * NOTE: Expected responses tend to vary because of time and other
         * factors. Make sure to remove expectations which you know the server
         * has no reason to keep.
         * 
         * @param name
         * @param diff
         * @return 
         */
        @Override
        public ExpectedResponse response(String name, BaseExpectedNode diff) {
            for(SampleResponseData r : data.responses) {
                if(r.name.equals(name)) {
                    return new ResponseVerifier(this, r.text, diff);
                }
            }
            throw new RuntimeException("Response named '" + name + "' not found.");
        }
    }
    
    /**
     * This is the root of the Postman Collection, a bunch of data from
     * the JSON file. Most of this is not actually used, things like ordering
     * and folders, etc. However, the "requests" is further traversed to
     * make this collection useful.
     */
    private static class PostmanCollectionData {
        private List<PostmanRequest> requests;
    }
    
    private final Map<String, RestRequest> names;
    private final RestSession session;
    
    /**
     * A PostmanCollection requires data from its JSON file. This class can
     * be constructed by passing a URL to a JSON file in the correct format.
     * 
     * @param data
     * @throws IOException 
     */
    private PostmanCollection(PostmanCollectionData data) throws IOException {
        names = new HashMap<String, RestRequest>();
        session = new RestSession();
        for(PostmanRequest req : data.requests) {
            names.put(req.data.name, req);
            req.session(session);
        }
    }
    
    /**
     * Get the Session for this collection, which is necessary for the use of
     * a login generated by one collection.
     * 
     * @return 
     */
    @Override
    public RestSession session() {
        return this.session;
    }
    
    /**
     * Create a Collection from a file. This takes in a JSON file and returns
     * a class of the PostmanCollection type.
     * 
     * @param collection
     * @return
     * @throws Exception 
     */
    public static RestCollection file(URL collection) throws Exception {   
        return new PostmanCollection(Json.Map.readValue(Okio.buffer(Okio.source((InputStream)collection.getContent())).readByteArray(), PostmanCollectionData.class));
    }

}
