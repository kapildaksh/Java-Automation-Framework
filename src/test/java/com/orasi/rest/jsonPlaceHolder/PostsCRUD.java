package com.orasi.rest.jsonPlaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.api.restServices.core.RestService_V2;
import com.orasi.api.restServices.services.gitHub.OrganizationRepos;
import com.orasi.api.restServices.services.gitHub.User;

public class PostsCRUD {
	
	/** 
	 *
	 */
	
	
	//@Test
	public void testPost() throws ClientProtocolException, IOException{
		String URL = "http://jsonplaceholder.typicode.com/posts";
		RestService_V2 restService = new RestService_V2();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("title", "My awesome title"));
		params.add(new BasicNameValuePair("body", "My awesome body"));
		params.add(new BasicNameValuePair("userID", "1"));
		
		restService.sendPostRequest(URL, params);
		
		//verify request comes back as 200 ok
		Assert.assertEquals(restService.getStatusCode(), HttpStatus.SC_OK);
		//verify format response is json
		Assert.assertEquals(restService.getResponseFormat(), "json");
	}
	
	//@Test
	public void testGET() throws ClientProtocolException, IOException{
		
		//instantiate the base rest service class
		RestService_V2 restService = new RestService_V2();
		//send in the get request
		String response = restService.sendGetRequest("http://jsonplaceholder.typicode.com/posts/1");
		//verify request comes back as 200 ok
		Assert.assertEquals(restService.getStatusCode(), HttpStatus.SC_OK);
		//verify format response is json
		Assert.assertEquals(restService.getResponseFormat(), "json");
		
		//create a tree of json response
		JsonNode node = restService.mapJSONToTree();
		
		//create an iterator of all the repo nodes
		Iterator<JsonNode> nodeIterator = node.iterator();
		
		//playing around with it
		 while (nodeIterator.hasNext()) {
	    	   JsonNode rootNode = nodeIterator.next();
	           // what is its type
	           System.out.println(rootNode.getNodeType());
	           // Prints Object
	    	   System.out.println(rootNode.toString()); 
	    }
	}
	
	@Test
	public void testPUT() throws ClientProtocolException, IOException{
		
		String URL = "http://jsonplaceholder.typicode.com/posts/1";
		RestService_V2 restService = new RestService_V2();
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("id", "1"));
		params.add(new BasicNameValuePair("title", "My awesome title"));
		params.add(new BasicNameValuePair("body", "My awesome body"));
		params.add(new BasicNameValuePair("userId", "1"));
		
		restService.sendPutRequest(URL, params);
		
		//verify request comes back as 200 ok
		Assert.assertEquals(restService.getStatusCode(), HttpStatus.SC_OK);
		//verify format response is json
		Assert.assertEquals(restService.getResponseFormat(), "json");
	}
	
	@Test
	public void testPATCH() throws ClientProtocolException, IOException{
		
	}

}
