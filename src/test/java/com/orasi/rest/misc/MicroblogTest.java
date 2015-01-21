/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.arven.sandbox.MockMicroblogServer;
import com.orasi.utils.rest.RestAssert;
import com.orasi.utils.rest.PostmanCollection;
import com.orasi.utils.rest.RestCollection;
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
    
    public static final String REST_SANDBOX = "/rest/sandbox/";
    
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
        Response res = collection.byName("Create User Tom").send();
        Assert.assertTrue(res.isSuccessful());        
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("User added.", n.path("message").asText());
    }
    
    @Test(groups = "users")
    public void createUserLarry() throws Exception {
        Response res = collection.byName("Create User Larry").send();
        Assert.assertTrue(res.isSuccessful());        
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("User added.", n.path("message").asText());        
    }

    @Test(groups = "usersVerify", dependsOnGroups = "users")
    public void verifyUserTom() throws Exception {
        Response res = collection.byName("Check User Tom").send();
        Assert.assertTrue(res.isSuccessful());        
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("tom", n.path("username").asText());
        Assert.assertEquals("Tom R. Fields", n.path("nickname").asText());
        Assert.assertEquals("tom@muckdragon.info", n.path("email").asText());
    }    
    
    @Test(groups = "usersVerify", dependsOnGroups = "users")
    public void verifyUserLarry() throws Exception {
        Response res = collection.byName("Check User Larry").send();
        Assert.assertTrue(res.isSuccessful());        
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("larry", n.path("username").asText());        
        Assert.assertEquals("Larry Wall", n.path("nickname").asText());
        Assert.assertTrue(n.path("email").isMissingNode());
    }
    
    @Test(groups = "posts", dependsOnGroups = "users")
    public void createPostTom() throws Exception {
        Response res = collection.byName("Tom Posts Message").send();
        Assert.assertTrue(res.isSuccessful());
    }
    
    @Test(groups = "posts", dependsOnGroups = "users")
    public void createPostLarry() throws Exception {
        Response res = collection.byName("Lots of Hash Tags").send();
        Assert.assertTrue(res.isSuccessful());
    }
    
    @Test(groups = "postsVerify", dependsOnGroups = "posts")
    public void verifyPostTom() throws Exception {
        Response res = collection.byName("Check Tom's Posts").send();
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        RestAssert.assertIsArray(n.path(0).path("tags"));
        Assert.assertEquals("This is an #example of a post which has a lot of those pointless #pound signs.", n.path(0).path("text").asText());
        RestAssert.assertInArray(n.path(0).path("tags"), "example");
        RestAssert.assertInArray(n.path(0).path("tags"), "pound");
    }
    
    @Test(groups = "postsVerify", dependsOnGroups = "posts")
    public void verifyPostLarry() throws Exception {
        Response res = collection.byName("Read Larry's Posts").send();
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        RestAssert.assertIsArray(n.path(0).path("tags"));
        Assert.assertEquals("I don't get those #tags anyway. Why #are #they #practically #before #every #word.", n.path(0).path("text").asText());
        RestAssert.assertInArray(n.path(0).path("tags"), "tags", "are", "they", "practically", "before", "every", "word");
    }
    
    @Test(groups = "postsVerify", dependsOnGroups = "posts")
    public void verifyPostLarrySingle() throws Exception {
        Response res = collection.byName("Another Way to Read a Post").send();
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        RestAssert.assertIsArray(n.path("tags"));
        Assert.assertEquals("I don't get those #tags anyway. Why #are #they #practically #before #every #word.", n.path("text").asText());
        RestAssert.assertInArray(n.path("tags"), "tags", "are", "they", "practically", "before", "every", "word");   
    }
    
    @Test(groups = "friends", dependsOnGroups = "usersVerify")
    public void addFriendTomLarry() throws Exception {
        Response res = collection.byName("Tom Adds Larry").send();
        Assert.assertTrue(res.isSuccessful());
    }
    
    @Test(groups = "friends", dependsOnGroups = "usersVerify")
    public void addFriendLarryTom() throws Exception {
        Response res = collection.byName("Larry Adds Tom Back").send();
        Assert.assertTrue(res.isSuccessful());        
    }
    
    @Test(groups = "friendsVerify", dependsOnGroups = "friends")
    public void verifyFriendTomLarry() throws Exception {
        Response res = collection.byName("Check User Tom").send();
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        RestAssert.assertIsArray(n.path("friends"));
        RestAssert.assertInArray(n.path("friends"), "larry");
    }
    
    @Test(groups = "friendsVerify", dependsOnGroups = "friends")
    public void verifyFriendLarryTom() throws Exception {
        Response res = collection.byName("Check User Larry").send();
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        RestAssert.assertIsArray(n.path("friends"));
        RestAssert.assertInArray(n.path("friends"), "tom");
    }
    
    @Test(groups = "friendsRemove", dependsOnGroups = "friendsVerify")
    public void removeFriendLarryTom() throws Exception {
        Response res = collection.byName("Larry Removes Tom").send();
        Assert.assertTrue(res.isSuccessful());        
    }
    
    @Test(groups = "friendsRemoveVerify", dependsOnGroups = "friendsRemove")
    public void verifyRemoveFriendLarryTom() throws Exception {
        Response res = collection.byName("Check User Larry").send();
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        RestAssert.assertNotArray(n.path("friends"));
    }
    
    @Test(groups = "patchEmail", dependsOnGroups = "usersVerify")
    public void patchLarryEmail() throws Exception {
        Response res = collection.byName("Larry Adds Email Address").send();
        Assert.assertTrue(res.isSuccessful());
    }
    
    @Test(groups = {"patchEmailVerify", "one"}, dependsOnGroups = "patchEmail")
    public void verifyPatchEmail() throws Exception {
        Response res = collection.byName("Check User Larry").send();
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("larry@wall.org", n.path("email").asText());        
    }

}
