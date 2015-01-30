/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.api.demos.MockMicroblogServer;
import com.orasi.utils.rest.RestAssert;
import com.orasi.utils.rest.PostmanCollection;
import com.orasi.utils.rest.RestCollection;
import com.orasi.utils.rest.RestResponse;
import com.squareup.okhttp.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The MicroBlog example server is a simple Jetty service which is started up
 * by this test. It is a volatile test double which is used to simulate some
 * web service like Twitter. It does not hold any state, but various actions
 * are performed on the in-memory data with a "clean slate" on every startup.
 * 
 * @author Brian Becker
 */
public class MicroblogTest {
    
    public static final String REST_SANDBOX = "/com/orasi/rest/misc/";
    
    public ObjectMapper map;
    public RestCollection collection;
    public MockMicroblogServer server;
    
    @BeforeClass
    public void setUp() throws Exception {
        map = new ObjectMapper();
        collection = PostmanCollection.file(getClass().getResource(REST_SANDBOX + "MicroBlog.json.postman_collection"));
        server = new MockMicroblogServer();
        server.start();
    }
    
    @AfterClass
    public void tearDown() {
        server.stop();
    }
        
    @Test(groups = "users")
    public void createUserTom() throws Exception {
        RestResponse res = collection.get("Create User Tom").send();
        Assert.assertTrue(res.success());        
        Assert.assertEquals("User added.", res.json().path("message").asText());
    }
    
    @Test(groups = "users")
    public void createUserLarry() throws Exception {
        RestResponse res = collection.get("Create User Larry").send();
        Assert.assertTrue(res.success());        
        Assert.assertEquals("User added.", res.json().path("message").asText());        
    }

    @Test(groups = "usersVerify", dependsOnGroups = "users")
    public void verifyUserTom() throws Exception {
        RestResponse res = collection.get("Check User Tom").send();
        Assert.assertTrue(res.success());        
        JsonNode n = res.json();
        Assert.assertEquals("tom", n.path("username").asText());
        Assert.assertEquals("Tom R. Fields", n.path("nickname").asText());
        Assert.assertEquals("tom@muckdragon.info", n.path("email").asText());
    }    
    
    @Test(groups = "usersVerify", dependsOnGroups = "users")
    public void verifyUserLarry() throws Exception {
        RestResponse res = collection.get("Check User Larry").send();
        Assert.assertTrue(res.success());        
        JsonNode n = res.json();
        Assert.assertEquals("larry", n.path("username").asText());        
        Assert.assertEquals("Larry Wall", n.path("nickname").asText());
        Assert.assertTrue(n.path("email").isMissingNode());
    }
    
    @Test(groups = "posts", dependsOnGroups = "users")
    public void createPostTom() throws Exception {
        collection.get("Login As Tom").send();
        RestResponse res = collection.get("Tom Posts Message").send();
        Assert.assertTrue(res.success());
    }
    
    @Test(groups = "posts", dependsOnGroups = "users")
    public void createPostLarry() throws Exception {
        collection.get("Login As Larry").send();
        RestResponse res = collection.get("Lots of Hash Tags").send();
        Assert.assertTrue(res.success());
    }
    
    @Test(groups = "postsVerify", dependsOnGroups = "posts")
    public void verifyPostTom() throws Exception {
        RestResponse res = collection.get("Check Tom's Posts").send();
        Assert.assertTrue(res.success());
        JsonNode n = res.json();
        RestAssert.assertIsArray(n.path(0).path("tags"));
        Assert.assertEquals("This is an #example of a post which has a lot of those pointless #pound signs.", n.path(0).path("text").asText());
        RestAssert.assertInArray(n.path(0).path("tags"), "example");
        RestAssert.assertInArray(n.path(0).path("tags"), "pound");
    }
    
    @Test(groups = "postsVerify", dependsOnGroups = "posts")
    public void verifyPostLarry() throws Exception {
        RestResponse res = collection.get("Read Larry's Posts").send();
        Assert.assertTrue(res.success());
        JsonNode n = res.json();
        RestAssert.assertIsArray(n.path(0).path("tags"));
        Assert.assertEquals("I don't get those #tags anyway. Why #are #they #practically #before #every #word.", n.path(0).path("text").asText());
        RestAssert.assertInArray(n.path(0).path("tags"), "tags", "are", "they", "practically", "before", "every", "word");
    }
    
    @Test(groups = "postsVerify", dependsOnGroups = "posts")
    public void verifyPostLarrySingle() throws Exception {
        RestResponse res = collection.get("Another Way to Read a Post").send();
        Assert.assertTrue(res.success());
        JsonNode n = res.json();
        RestAssert.assertIsArray(n.path("tags"));
        Assert.assertEquals("I don't get those #tags anyway. Why #are #they #practically #before #every #word.", n.path("text").asText());
        RestAssert.assertInArray(n.path("tags"), "tags", "are", "they", "practically", "before", "every", "word");   
    }
    
    @Test(groups = "friends", dependsOnGroups = "usersVerify")
    public void addFriendTomLarry() throws Exception {
        collection.get("Login As Tom").send();
        RestResponse res = collection.get("Tom Adds Larry").send();
        Assert.assertTrue(res.success());
    }
    
    @Test(groups = "friends", dependsOnGroups = "usersVerify")
    public void addFriendLarryTom() throws Exception {
        collection.get("Login As Larry").send();
        RestResponse res = collection.get("Larry Adds Tom").send();
        Assert.assertTrue(res.success());        
    }
    
    @Test(groups = "friendsVerify", dependsOnGroups = "friends")
    public void verifyFriendTomLarry() throws Exception {
        RestResponse res = collection.get("Check User Tom").send();
        Assert.assertTrue(res.success());
        JsonNode n = res.json();
        RestAssert.assertIsArray(n.path("friends"));
        RestAssert.assertInArray(n.path("friends"), "larry");
    }
    
    @Test(groups = "friendsVerify", dependsOnGroups = "friends")
    public void verifyFriendLarryTom() throws Exception {
        RestResponse res = collection.get("Check User Larry").send();
        Assert.assertTrue(res.success());
        RestAssert.assertIsArray(res.json().path("friends"));
        RestAssert.assertInArray(res.json().path("friends"), "tom");
    }
    
    @Test(groups = "friendsRemove", dependsOnGroups = "friendsVerify")
    public void removeFriendLarryTom() throws Exception {
        collection.get("Login As Larry").send();
        RestResponse res = collection.get("Larry Removes Tom").send();
        Assert.assertTrue(res.success());        
    }
    
    @Test(groups = "friendsRemoveVerify", dependsOnGroups = "friendsRemove")
    public void verifyRemoveFriendLarryTom() throws Exception {
        RestResponse res = collection.get("Check User Larry").send();
        RestAssert.assertNotArray(res.json().path("friends"));
    }
    
    @Test(groups = "patchEmail", dependsOnGroups = "usersVerify")
    public void patchLarryEmail() throws Exception {
        collection.get("Login As Larry").send();
        RestResponse res = collection.get("Larry Adds Email Address").send();
        Assert.assertTrue(res.success());
    }
    
    @Test(groups = {"patchEmailVerify", "one"}, dependsOnGroups = "patchEmail")
    public void verifyPatchEmail() throws Exception {
        RestResponse res = collection.get("Check User Larry").send();
        Assert.assertEquals("larry@wall.org", res.json().path("email").asText());        
    }

}
