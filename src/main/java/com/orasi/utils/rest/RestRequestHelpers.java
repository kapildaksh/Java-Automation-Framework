package com.orasi.utils.rest;

import com.orasi.text.TemplateFormat;
import com.orasi.utils.rest.RestRequest.RequestData;
import com.orasi.utils.rest.RestRequest.RequestType;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.ssl.Base64;

/**
 * These are helper functions for generating parts of Rest requests. A string
 * block can be converted into OkHttp headers, etc.
 * 
 * @author Brian Becker
 */
public class RestRequestHelpers {
    
    /**
     * Generate the key and value strings for the request data by processing
     * them through the variable formatter.
     * 
     * @param data
     * @param variables
     * @return 
     */
    public static List<RequestData> variables(List<RequestData> data, Map variables) {
        List<RequestData> data2 = new LinkedList<RequestData>();
        for(RequestData dt : data) {
            RequestData dt2 = new RequestData();
            dt2.key = format(dt.key, variables);
            dt2.value = format(dt.value, variables);
            dt2.enabled = dt.enabled;
            dt2.type = dt.type;
            data2.add(dt2);
        }
        return data2;
    }
    
    public static String variables(String url, Map variables) {
        return format(url, variables);
    }
    
    public static String format(String key, Map variables) {
        try {
            return (new TemplateFormat(key, "{{", "}}", '$')).format(variables);
        } catch (Exception e) {
            return key;
        }
    }
    
    /**
     * Generate a set of headers from a simple string split by newlines.
     * 
     * @param headers
     * @param auth
     * @param variables
     * @return 
     */
    public static Headers headers(String headers, Map auth, Map variables) {
        Headers.Builder hdrs = new Headers.Builder();
        boolean authorizationFound = false;
        for(String hdr : headers.split(Pattern.quote("\n"))) {
            String[] h = hdr.split(Pattern.quote(":"), 2);
            if(h.length == 2) {
                if(!h[0].equals("Authorization")) {
                    hdrs = hdrs.add(h[0], h[1]);
                } else {
                    authorizationFound = true;
                }
            }
            if(authorizationFound && auth.get("username") != null) {
                hdrs = hdrs.add(h[0], "Basic " + Base64.encodeBase64String(StringUtils.getBytesUtf8(format(auth.get("username") + ":" + auth.get("password"), variables))));
            }
        }
        return hdrs.build();
    }
    
    /**
     * Create a request based on a request type, a string header, url, and body.
     * 
     * @param type
     * @param headers
     * @param url
     * @param auth
     * @param format
     * @param data
     * @param rawModeData
     * @param variables
     * @param parameters
     * @return 
     * @throws java.lang.Exception 
     */
    public static Request request(RestRequest.RequestType type, String headers, String url, Map auth, RestRequest.RequestFormat format, List<RequestData> data, String rawModeData, Map variables, List<String> parameters) throws Exception {
        url = RestRequestHelpers.variables(url, variables);
        rawModeData = RestRequestHelpers.variables(rawModeData, variables);
        if(data != null) {
            data = RestRequestHelpers.variables(data, variables);
        }
        
        RequestBody body = null;
        switch(format) {
            case URLENCODE:
                Pair<RequestBody, String> p = RestRequestHelpers.urlencode(type, url, data);
                body = p.getLeft();
                url = p.getRight();
                break;
            case RAW:
                if(rawModeData != null)
                    body = RestRequestHelpers.raw(rawModeData);
                else
                    body = RestRequestHelpers.binary(parameters);
                break;
            case MULTIPART_FORM:
                if(data != null && data.size() > 0) {
                    body = RestRequestHelpers.params(data, parameters);
                }
                break;
        }
               
        return new Request.Builder()
                .url(url)
                .headers(headers(headers, auth, variables))
                .method(type.toString(), type.equals(RequestType.GET) ? null : body).build();
    }
    
    /**
     * Encode a request body with URL encoder. If it's a get request, create or
     * add to the URL parameters.
     * 
     * @param type
     * @param url
     * @param data
     * @return
     * @throws Exception 
     */
    public static Pair<RequestBody, String> urlencode(RestRequest.RequestType type, String url, List<RequestData> data) throws Exception {
        StringBuilder text = new StringBuilder("");
        RequestBody body = null;
        for(RestRequest.RequestData dt : data) {
            text.append(URLEncoder.encode(dt.key, "UTF-8"));
            text.append("=");
            text.append(URLEncoder.encode(dt.value, "UTF-8"));
            text.append("&");
        }
        text.deleteCharAt(text.length() - 1);
        if(type.equals(RestRequest.RequestType.GET)) {
            if(url.endsWith("?")) {
                url = url + "&" + text.toString();
            } else {
                url = url + "?" + text.toString();
            }
        } else {
            body = RequestBody.create(null, text.toString());
        }
        return Pair.of(body, url);
    }
    
    /**
     * Generate a multi-part form type body, which consists of a list of
     * request data. This format can handle sending files, the parameters
     * are the file contents, not file name.
     * 
     * @param data
     * @param parameters
     * @return 
     */
    public static RequestBody params(List<RequestData> data, List<String> parameters) {
        int fileIdx = 0;
        MultipartBuilder mb = new MultipartBuilder();
        mb.type(MultipartBuilder.FORM);
        for(RestRequest.RequestData dt : data) {
            switch (dt.type) {
                case "text":
                    mb.addFormDataPart(dt.key, dt.value);
                    break;
                case "file":
                    if(fileIdx < parameters.size()) {
                        mb.addFormDataPart(dt.key, parameters.get(fileIdx++));
                    }
                    break;
            }
        }
        return mb.build();
    }
    
    /**
     * Raw mode passes a single string into the request body which has been
     * generated through some method such as a JSON generator.
     * 
     * @param rawModeData
     * @return 
     */
    public static RequestBody raw(String rawModeData) {
        return RequestBody.create(null, rawModeData);
    }
    
    /**
     * Simple case of the params request body function, there is only one
     * file.
     * 
     * @param parameters
     * @return 
     */
    public static RequestBody binary(List<String> parameters) {
        return RequestBody.create(null, parameters.get(0));
    }
}
