package com.orasi.utils.rest;

import com.orasi.text.Template;
import com.orasi.utils.rest.RestRequest.RequestData;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
     * @param   data            List of data entries for non-raw data
     * @param   variables       Map of variables
     * @return  List of processed request data
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
    
    /**
     * Generate the correct URL string for the request by processing it through
     * the variable formatter. This does not replace the parameters in the
     * URL, solely the actual templated variables which are either in a
     * global or local scope. Non-resolvable variables will remain the variable
     * name encased in the {{ }} string.
     * 
     * @param   type            Type of request
     * @param   url             Unprocessed URL
     * @param   data            Unprocessed Data
     * @return  Processed URL
     */
    public static String url(RestRequest.RequestType type, String url, List<RequestData> data) {
        //if(params != null) {
        //    for(Object e : params.entrySet()) {
        //        String key = ((Entry)e).getKey().toString();
        //        String val = ((Entry)e).getValue().toString();
        //        if(url.contains(":" + key)) {
        //            url = url.replace(":" + key, val);
        //        }
        //    }
        //}
        //url = format(url, variables);
        url = urlencode(type, url, data).getRight();
        return url;
    }    
    
    public static String format(String key, Map variables) {
        try {
            return (new Template(key, "{{", "}}", '$')).format(variables);
        } catch (Exception e) {
            return key;
        }
    }
    
    /**
     * Generate a set of headers from a simple string split by newlines. This
     * method will additionally remove any Authorization headers and replace
     * with the proper encoded values. The Authorization header is not
     * necessary when there is authentication data.
     * 
     * NOTE: Rework this for holding auth header data in a non-plaintext
     * form. We could just go ahead and preprocess with Base64, etc. We
     * should probably instead use variables, as that would break compat
     * with actual Postman for editing.
     * 
     * @param   headers     String containing \n separated headers
     * @param   auth        Map of username, password, etc.
     * @param   variables   Substitution variables
     * @return  OkHttp Headers object
     */
    public static Headers headers(String headers) {
        Headers.Builder hdrs = new Headers.Builder();
        boolean authorizationFound = false;
        for(String hdr : headers.split(Pattern.quote("\n"))) {
            String[] h = hdr.split(Pattern.quote(":"), 2);
            if(h.length == 2) {
                hdrs = hdrs.add(h[0], h[1]);
            }
        }
        return hdrs.build();
    }
    
    /**
     * The main REST request generation method. This is a long method designed
     * solely to delegate to headers and auth processing, url processing, data
     * processing, as well as files and parameters attachment.
     * 
     * @param   type            GET, POST, PUT, PATCH...
     * @param   format          URL Encoded, RAW, Multipart Form...
     * @param   data            Unsubstituted, unserialized data
     * @param   rawModeData     Raw data to be sent
     * @param   parameters      Parameters for the various data formats
     * @return  Request in the form of an OkHttp request
     * @throws  java.lang.Exception 
     */
    public static RequestBody body(RestRequest.RequestType type, RestRequest.RequestFormat format, List<RequestData> data, String rawModeData, List<String> parameters) throws Exception {
        RequestBody body = null;
        switch(format) {
            case URLENCODE:
                //if(!type.equals(RestRequest.RequestType.GET)) {
                    Pair<RequestBody, String> p = RestRequestHelpers.urlencode(type, "", data);
                    body = p.getLeft();
                //}
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
        return body;
    }
    
    /**
     * Encode a request body with URL encoder. If it's a GET request, create or
     * add to the URL parameters.
     * 
     * @param   type        type of request (we need to know if it's GET)
     * @param   url         url for GET requests
     * @param   data        data to serialize
     * @param   variables   variables for substitution
     * @return  ( new body, new URL string) 
     */
    public static Pair<RequestBody, String> urlencode(RestRequest.RequestType type, String url, List<RequestData> data) {
        StringBuilder text = new StringBuilder("");
        RequestBody body = null;
        if(data != null && !data.isEmpty()) {
            for(RestRequest.RequestData dt : data) {
                try {
                    text.append(URLEncoder.encode(dt.key, "UTF-8"));
                    text.append("=");
                    text.append(URLEncoder.encode(dt.value, "UTF-8"));
                    text.append("&");
                } catch (UnsupportedEncodingException ex) {
                    throw new RuntimeException("Unsupported Encoding");
                }
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
        }
        return Pair.of(body, url);
    }
    
    /**
     * Generate a multi-part form type body, which consists of a list of
     * request data. This format can handle sending files, the parameters
     * are the file contents, not file name.
     * 
     * @param   data        data to serialize
     * @param   parameters  parameters, which are file contents
     * @return  Request Body for message to send
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
     * @param   rawModeData data to serialize
     * @return  Request Body for message to send
     */
    public static RequestBody raw(String rawModeData) {
        return RequestBody.create(null, rawModeData);
    }
    
    /**
     * Simple case of the params request body function, there is only one
     * file.
     * 
     * @param   parameters  a request which consists of a single file
     * @return  Request Body for message to send
     */
    public static RequestBody binary(List<String> parameters) {
        return RequestBody.create(null, parameters.get(0));
    }
}
