package com.orasi.sandbox;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.apache.commons.io.IOUtils;







import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.api.restServices.core.RestService;
import com.orasi.api.restServices.core.RestService_V2;
import com.orasi.api.restServices.services.gitHub.User;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.ExcelDataProvider;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Iterator;

public class Jess {
	
	@Test
	public void gitHubUserTest() throws ClientProtocolException, IOException{
	
		//instantiate the base rest service class
		RestService_V2 restService = new RestService_V2();
		//send in the get request
		restService.sendGetRequest("https://api.github.com/users/jessicamarshall");
		//verify request comes back as 200 ok
		Assert.assertEquals(restService.getStatusCode(), HttpStatus.SC_OK);
		//verify format response is json
		Assert.assertEquals(restService.getResponseFormat(), "json");
		//take the json response string and transform to java object
		User user = restService.mapJSONToObject(User.class);
		
		//outputting the 
		
	    //User user = mapper.readValue(jsonFromResponse, User.class);
		   
	   /*
	   String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	   System.out.println(jsonFromResponse);
	   ObjectMapper mapper = new ObjectMapper().
			      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	   JsonNode node = mapper.readTree(jsonFromResponse);
	   System.out.println(node.getNodeType());
	   System.out.println(node.isContainerNode());
	   
	   Iterator<String> fieldNames = node.fieldNames();
	   int x=0;
       while (fieldNames.hasNext()) {
           String fieldName = fieldNames.next();
           System.out.println(fieldName); 
       }   
       System.out.println(node.get("login").asText());
	   System.out.println(node.path("testing"));
       System.out.println(node.get("login").getNodeType());
       
       Iterator<JsonNode> responseIterator = node.iterator();
       while (responseIterator.hasNext()) {
    	   JsonNode datasetElement = responseIterator.next();
           // what is its type
           System.out.println(datasetElement.getNodeType());// Prints Object
    	   System.out.println(datasetElement.toString()); 
       }
       */

	   
       
	}

}
