package com.orasi.rest.redditAPI;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.api.restServices.core.RestService;
import com.orasi.api.restServices.services.reddit.SubRedditPage;

public class SubRedditPageTest {
	
	/** Playing around with different ways of testing this service, map each root node to a hash map
	 * 	, take each root node and make a Jackson tree of it.
	 *	With the last option, you could take the tree for each root node, find the repo you're interested in,
	 *	then do something with it?  Maybe get the listing of teams for this repo using teams_url, have it in 
	 *	a variable and save for another test to send in another request? 
	 *
	 */
	
	String teamsURL=null;
	@Test
	public void orgReposTreeTest() throws ClientProtocolException, IOException{
		String subReddit = "diy";
		//instantiate the base rest service class
		RestService restService = new RestService();
		//send in the get request
		restService.setUserAgent("Agent");
		restService.sendGetRequest("http://www.reddit.com/r/" + subReddit + "/.json");
		
		//verify request comes back as 200 ok
		Assert.assertEquals(restService.getStatusCode(), HttpStatus.SC_OK);
		//verify format response is json
		Assert.assertEquals(restService.getResponseFormat(), "json");
		System.out.println();
		
		//create a tree of json response
		JsonNode node = restService.mapJSONToTree();
		System.out.println("Number of children : " + node.get("data").get("children").size());
		System.out.println();
		

		//create an iterator of all the nodes
		Iterator<JsonNode> nodeIterator = node.get("data").get("children").iterator();
		
		while (nodeIterator.hasNext()) {

		   JsonNode data = nodeIterator.next();
	    	   SubRedditPage page = restService.mapJSONToObject(data.get("data").toString(), SubRedditPage.class);
	    	   
	    	   System.out.println("Title: " + page.getTitle());
	    	   System.out.println("Reddit ID: " + page.getId());
	    	   System.out.println("URL: " + page.getUrl());
	    	   System.out.println("Permalink: " + page.getPermalink()); 	    	   
	    	   System.out.println("Author: " + page.getAuthor());
	    	   System.out.println("Likes: " + page.getLikes());
	    	   System.out.println("Score: " + page.getScore());
	    	   System.out.println("Is Stickied: " + page.getStickied());
	    	   System.out.println("---------------------------");
	    	   System.out.println();
		 }
	}

}
