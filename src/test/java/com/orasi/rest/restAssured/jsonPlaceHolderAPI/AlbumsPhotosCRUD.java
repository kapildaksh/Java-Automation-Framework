package com.orasi.rest.restAssured.jsonPlaceHolderAPI;

import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;

public class AlbumsPhotosCRUD {

	@Test
	public void testIt(){
		
		RestAssured.baseURI = "http://jsonplaceholder.typicode.com/";
		expect().body("id", equalTo(1)).when().get("albums/1");
		expect().body("title", equalTo("quidem molestiae enim")).when().get("albums/1");
	}
}
