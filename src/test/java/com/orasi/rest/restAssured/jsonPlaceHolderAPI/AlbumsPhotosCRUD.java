package com.orasi.rest.restAssured.jsonPlaceHolderAPI;

import java.util.List;

import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;
import static   com.jayway.restassured.RestAssured.*;
import static   com.jayway.restassured.matcher.RestAssuredMatchers.*;

public class AlbumsPhotosCRUD {

	@Test
	public void testIt(){
		
		//Just showing a couple of variations of phrasing the validations
		
		//simple validations on json response
		RestAssured.baseURI = "http://jsonplaceholder.typicode.com/";
		expect().statusCode(200).when().get("albums/1");
		expect().body("id", equalTo(1)).when().get("albums/1");
		expect().body("title", equalTo("quidem molestiae enim")).when().get("albums/1");
		
		
		//reframe the assertion
		get("albums/1").then().body("title", equalTo("quidem molestiae enim"));
		get("albums/1").then().statusCode(200);
		
		//use given/expect/when
		expect()
			.statusCode(200)
			.body("id", equalTo(1)).
		when()
			.get("albums/1");
		
		//use given/when/then
		when()
			.get("albums/1")
		.then()
			.statusCode(200)
			.body("title", equalTo("quidem molestiae enim"));
		
		//extract a value
		String albumTitle = 
		when()
			.get("albums/1")
		.then()
			.statusCode(200)
		.extract().path("title");
		System.out.println(albumTitle);
		
		//extract the response
		Response response = 
		when()
			.get("albums")
		.then()
			.statusCode(200)
		.extract().response();
		
		//print out the response
		System.out.println("Response: " + response.asString());
		//print out the headers
		System.out.println("Headers: " + response.headers());
		//print out all the cookies
		System.out.println("Cookies: " + response.getCookies());
		//print out all nodes matching title
		System.out.println(response.path("title"));
		//putting it all in a list
		List<String> titles = response.path("title");
		for (String title:titles){
			if (title.equalsIgnoreCase("enim repellat iste")){
				System.out.println(title);
			}
		}
		

		
		
		
		
		
	}
	
	@Test
	public void testPost(){
		
		RestAssured.baseURI = "http://jsonplaceholder.typicode.com/";
		
		
	}
}
