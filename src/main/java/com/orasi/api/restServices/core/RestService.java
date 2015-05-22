package com.orasi.api.restServices.core;

/**
 * Just playing around with some different ways of using rest services with Jackson 
 */
import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



public class RestService {
	int statusCode = 0;
	String responseFormat;
	String responseAsString = null;
	String userAgent; 
	
	private ObjectMapper mapper = new ObjectMapper().
		      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	//constructor
	public RestService() {}
	
	/*
	 * Encapsulation area 
	 */
	
	public String getUserAgent(){ return this.userAgent;}
	
	public void setUserAgent(String userAgent){ this.userAgent = userAgent;	}	
	
	public int getStatusCode(){ return statusCode; }
	
	private void setStatusCode(HttpResponse httpResponse){ 	statusCode = httpResponse.getStatusLine().getStatusCode(); }
	
	public String getResponseFormat(){ return responseFormat; }
	
	private void setResponseFormat(HttpResponse httpResponse){ responseFormat = ContentType.getOrDefault(httpResponse.getEntity()).getMimeType().replace("application/", "");	}
	
	/**
	 * Sends a GET request
	 * 
	 * @param 	URL for the service you are testing
	 * @return 	response in string format
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
	public String sendGetRequest(String URL) throws ClientProtocolException, IOException{
		HttpUriRequest request = new HttpGet(URL);
		HttpResponse httpResponse = HttpClientBuilder.create().setUserAgent(getUserAgent()).build().execute( request );
		
		setStatusCode(httpResponse);		
		setResponseFormat(httpResponse);
		
		responseAsString = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("String response: " + responseAsString);
		
		return responseAsString;
	}
	
	/**
	 * Sends a post (update) request, pass in the parameters for the json arguments to update
	 * 
	 * @param 	URL		for the service
	 * @param 	params	arguments to update
	 * @return 	response in string format
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
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
	
	
	/**
	 * Sends a put (create) request, pass in the parameters for the json arguments to create
	 * 
	 * @param 	URL		for the service
	 * @param 	params	arguments to update
	 * @return 	response in string format
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
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
	
	/**
	 * Sends a patch (update) request, pass in the parameters for the json arguments to update
	 * 
	 * @param 	URL		for the service
	 * @param 	params	arguments to update
	 * @return 	response in string format
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
	public String sendPatchRequest(String URL, List<NameValuePair> params) throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		HttpPatch patchRequest = new HttpPatch(URL);
		patchRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		
		HttpResponse httpResponse = httpclient.execute(patchRequest);
		setStatusCode(httpResponse);		
		setResponseFormat(httpResponse);
		
		responseAsString = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("String response: " + responseAsString);
		
		return responseAsString;
	}
	
	/**
	 * Sends a delete request.  Depends on the service if a response is returned.
	 * If no response is returned, will return null	 * 
	 * 
	 * @param 	URL		for the service
	 * @return 	response in string format or null
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
	public String sendDeleteRequest(String URL) throws ClientProtocolException, IOException{

		HttpUriRequest deleteRequest = new HttpDelete(URL);
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute( deleteRequest );

		setStatusCode(httpResponse);		
		setResponseFormat(httpResponse);
		
		if (httpResponse.getEntity()!=null){
			responseAsString = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("String response: " + responseAsString);
		}		
		
		return responseAsString;
	}
	
	/**
	 * Sends an options request.  Options should give what the acceptable methods are for
	 * the service (GET, HEAD, PUT, POST, etc).  There should be some sort of an ALLOW 
	 * header that will give you the allowed methods.  May or may not be a body to the response, 
	 * depending on the service.  
	 * 
	 * This method will return all the headers and the test should parse through and find the header 
	 * it needs, that will give the allowed methods, as the naming convention will be different for each service.  
	 * 
	 * @param 	URL		for the service
	 * @return 	returns an array of headers
	 * @throws 	ClientProtocolException
	 * @throws 	IOException
	 */
	public Header[] sendOptionsRequest(String URL ) throws ClientProtocolException, IOException{
		HttpClient httpclient = HttpClients.createDefault();
		HttpOptions httpOptions=new HttpOptions(URL);
		
		HttpResponse httpResponse=httpclient.execute(httpOptions);
		System.out.println("Response Headers: ");
		Header[] headers = httpResponse.getAllHeaders();
		for (Header header: headers ){	
			System.out.println(header.getName() + " : " + header.getValue());
		}
		
		setStatusCode(httpResponse);		
		setResponseFormat(httpResponse);
		
		return headers;
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
