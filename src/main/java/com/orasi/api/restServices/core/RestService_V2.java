package com.orasi.api.restServices.core;

/**
 * Just playing around with some different ways of using rest services with Jackson 
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



public class RestService_V2 {


	
	
	int statusCode = 0;
	String responseFormat;
	String responseAsString;
	
	private ObjectMapper mapper = new ObjectMapper().
		      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	//constructor
	public RestService_V2() {
		
	}
	
	/**
	 * Sends a GET request
	 * @param URL 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String sendGetRequest(String URL) throws ClientProtocolException, IOException{
		HttpUriRequest request = new HttpGet(URL);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
		
		setStatusCode(httpResponse);		
		setResponseFormat(httpResponse);
		
		responseAsString = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("String response: " + responseAsString);
		
		return responseAsString;
	}
	
	/**
	 * Sends a post request
	 * @param URL
	 * @param params
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String sendPostRequest(String URL, List<NameValuePair> params) throws ClientProtocolException, IOException{
		
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(URL);
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		
		HttpResponse httpResponse = httpclient.execute(httppost);
		setStatusCode(httpResponse);		
		setResponseFormat(httpResponse);
		
		responseAsString = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("String response: " + responseAsString);
		
		return responseAsString;
	}

	public String sendPutRequest(String URL, List<NameValuePair> params) throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		HttpPut putRequest = new HttpPut(URL);
		putRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		
		HttpResponse httpResponse = httpclient.execute(putRequest);
		setStatusCode(httpResponse);		
		setResponseFormat(httpResponse);
		
		responseAsString = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("String response: " + responseAsString);
		
		return responseAsString;
	}
	
	
	private void setStatusCode(HttpResponse httpResponse){
		statusCode = httpResponse.getStatusLine().getStatusCode();
		System.out.println("Status code: " + statusCode);
	}

	public int getStatusCode(){
		return statusCode;
	}
	
	public String getResponseFormat(){
		return responseFormat;
	}
	
	private void setResponseFormat(HttpResponse httpResponse){
		responseFormat = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType().replace("application/", "");
		System.out.println(responseFormat);
	}
	
	/**
	 * Uses the class instance of the responeAsString to map to object
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public <T> T mapJSONToObject(Class<T> clazz) throws IOException {
		return mapJSONToObject(responseAsString, clazz);
		
	}
	
	/**
	 * Can pass in any json as a string and map to object
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public <T> T mapJSONToObject(String stringResponse, Class<T> clazz) throws IOException {
		
		return mapper.readValue(stringResponse, clazz);
	}
	
	/**
	 * Can pass in any json as a string and maps to tree
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public JsonNode mapJSONToTree(String stringResponse) throws IOException {
				
		return mapper.readTree(stringResponse);
	}
	
	/**
	 * Uses the class instance of the responeAsString to map to tree
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	public JsonNode mapJSONToTree() throws IOException {
		return mapJSONToTree(responseAsString);
	}
	
}
