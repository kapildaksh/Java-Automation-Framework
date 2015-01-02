package com.orasi.api.restServices.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RestService {
    private String defaultResponseFormat = "json";
    
    private boolean valiateAcceptableFormat(String format){
	if (format == "xml" || format == "json") return true;
	return false;
    }
    
    public void setDefaultResponseFormat(String format){
	if(valiateAcceptableFormat(format)){
	    this.defaultResponseFormat = format.toLowerCase();
	} else {
	    throw new RuntimeException("Invalid response format entered. Acceptable formats are 'json' or 'xml'");
	}
    }
    
    public String getDefaultResponseFormat(){
	return defaultResponseFormat;
    }
    
    public String sendGetRequest(String url){
	return sendGetRequest(url, getDefaultResponseFormat());
    }
    
    public String sendGetRequest(String url, String responseFormat){

	StringBuilder rawResponse = new StringBuilder();

	HttpURLConnection conn = httpConnectionBuilder(url,"GET", responseFormat);

	InputStream stream = null;
	String buffer = "";
	
	try {
	    stream = conn.getInputStream();
	    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(stream));
	    while ((buffer = bufferReader.readLine()) != null) {
		rawResponse.append(buffer);
	    }	
	} catch (IOException ioe) {
	    throw new RuntimeException(ioe.getMessage());
	}

	return rawResponse.toString();
    }

    private URL urlBuilder(String url){

	URL urlRequest = null;

	try {
	    urlRequest = new URL(url);
	} catch (MalformedURLException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	return urlRequest;
    }
    
    private HttpURLConnection httpConnectionBuilder(String url, String requestMethod, String responseFormat){
	HttpURLConnection connection = null;
	URL urlRequest = urlBuilder(url);
	
	try {
	    connection = (HttpURLConnection) urlRequest.openConnection();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	connection.setDoOutput(true);
	connection.setDoInput(true);
	if(valiateAcceptableFormat(responseFormat)) connection.setRequestProperty("Accept", "application/" + responseFormat);
	
	try {
	    connection.setRequestMethod(requestMethod.toUpperCase());
	} catch (ProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return connection;
    }
}
