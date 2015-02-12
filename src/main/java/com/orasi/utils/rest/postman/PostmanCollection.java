package com.orasi.utils.rest.postman;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.text.Data;
import com.orasi.text.Template;
import com.orasi.utils.rest.BaseExpectedNode;
import com.orasi.utils.rest.ExpectedResponse;
import com.orasi.utils.rest.Json;
import com.orasi.utils.rest.OkRestResponse;
import com.orasi.utils.rest.ResponseVerifier;
import com.orasi.utils.rest.RestCollection;
import com.orasi.utils.rest.RestRequest;
import com.orasi.utils.rest.RestRequestBuilder;
import com.orasi.utils.rest.RestRequestHelpers;
import com.orasi.utils.rest.RestResponse;
import com.orasi.utils.rest.RestSession;
import com.orasi.utils.rest.RxRestResponse;
import com.orasi.utils.types.DefaultingMap;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.xalan.xsltc.compiler.Constants;

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
            Map variables = new DefaultingMap(env(), session().env());            
            
            Client client = ClientBuilder.newClient().register(session());
            WebTarget target = client.target(Template.sinatra(Template.braces(data.url, variables), params()));
            
            Entity payload = null;
            
            RequestFormat format = null;
            switch(data.dataMode) {
                case "params": format = RequestFormat.MULTIPART_DATA; break;
                case "urlencoded": format = RequestFormat.ENCODED_DATA; break;
                case "binary": format = RequestFormat.MULTIPART_DATA; break;
                case "raw": format = RequestFormat.RAW_DATA; break;
            }
            
            System.out.println(format);
            if(data.data != null && data.method == RequestType.GET) {
                for(RequestData d : data.data) {
                    target = target.queryParam(Template.braces(d.key, variables), Template.braces(d.value, variables));
                }
            } else if(format == RequestFormat.ENCODED_DATA && data.data != null && data.method != RequestType.GET) {
                Form form = new Form();
                for(RequestData d : data.data) {
                    form.param(Template.braces(d.key, variables), Template.braces(d.value, variables));
                }
                payload = Entity.form(form);
            } else if(format == RequestFormat.RAW_DATA && data.rawModeData != null) {
                payload = Entity.entity(Template.braces(data.rawModeData, variables), MediaType.WILDCARD_TYPE);
            } else if(format == RequestFormat.MULTIPART_DATA && data.data != null && data.data.size() > 0) {
                List<Attachment> attachments = new ArrayList<Attachment>();
                for(RequestData d : data.data) {
                    attachments.add(new Attachment(Template.braces(d.key, variables), "text/plain", Template.braces(d.value, variables)));
                }
                payload = Entity.entity(new MultipartBody(attachments), MediaType.MULTIPART_FORM_DATA_TYPE);
            } else if(format == RequestFormat.MULTIPART_DATA && data.data == null) {
                if(files().size() == 1) {
                    URI file = files().get(0);
                    System.out.println(file.toURL().getFile());
                    payload = Entity.entity(file.toURL().getContent(), MediaType.APPLICATION_OCTET_STREAM_TYPE);
                }
            }
            
            MultivaluedMap headers = new MultivaluedHashMap();
            
            for (String hdr : data.headers.split(Pattern.quote("\n"))) {
                if(hdr.contains(":")) {
                    String v[] = hdr.split(Pattern.quote(":"), 2);
                    headers.add(v[0], v[1]);
                }
            }
            
            Response response = target
                    .request()
                    .headers(headers)
                    .method(data.method.name(), payload);
            
            return new RxRestResponse(response);
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
     */
    public static RestCollection file(URL collection) {   
        try {
            return new PostmanCollection(Json.Map.readValue(Data.get(collection), PostmanCollectionData.class));
        } catch (IOException ex) {
            throw new RuntimeException("Invalid Collection File Specified");
        }
    }

}
