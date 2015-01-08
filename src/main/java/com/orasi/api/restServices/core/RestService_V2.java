package com.orasi.api.restServices.core;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class RestService_V2 {

	HttpUriRequest request;
	HttpResponse httpResponse;
	int statusCode = 0;
	String responseFormat;
	String responseAsString;
	
	
	//constructor
	public RestService_V2() {
		
	}
	
	public String sendGetRequest(String URL) throws ClientProtocolException, IOException{
		request = new HttpGet(URL);
		httpResponse = HttpClientBuilder.create().build().execute( request );
		
		setStatusCode();		
		setResponseFormat();
		
		responseAsString = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("String response: " + responseAsString);
		
		return responseAsString;
	}
	
	private void setStatusCode(){
		statusCode = httpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code: " + statusCode);
	}

	public int getStatusCode(){
		return statusCode;
	}
	
	public String getResponseFormat(){
		return responseFormat;
	}
	
	public void setResponseFormat(){
		responseFormat = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType().replace("application/", "");
		System.out.println(responseFormat);
	}
	
	public <T> T mapJSONToObject(Class<T> clazz) throws IOException {
		ObjectMapper mapper = new ObjectMapper().
				      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(responseAsString, clazz);
	}
	
}
