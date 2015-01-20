/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.arven.sandbox.MockMicroblogServer;
import com.orasi.utils.rest.PostmanCollection;
import com.orasi.utils.rest.PostmanCollection.PostmanRequest;
import com.orasi.utils.rest.RestAssert;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.junit.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Brian Becker
 */
public class MicroblogTest {
    
    public static final String REST_SANDBOX = "/rest/sandbox/";
    
    public ObjectMapper map;
    public PostmanCollection collection;
    public MockMicroblogServer server;
    
    @BeforeClass
    public void setUp() throws IOException, URISyntaxException {
        map = new ObjectMapper();
        collection = PostmanCollection.fromPath(Paths.get(getClass().getResource(REST_SANDBOX).toURI()).resolve("MicroBlog.json.postman_collection"));
        server = new MockMicroblogServer();
        Thread t = new Thread(server);
        t.start();
    }
    
    @AfterClass
    public void tearDown() {
        server.stop();
    }
        
    @Test(groups = "users")
    public void createUserTom() throws Exception {
        PostmanRequest req = collection.getRequestByName("Create User Tom");
        Response res = req.send();
        Assert.assertTrue(res.isSuccessful());        
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("User added.", n.path("message").asText());
    }
    
    @Test(groups = "users")
    public void createUserLarry() throws Exception {
        PostmanRequest req = collection.getRequestByName("Create User Larry");
        Response res = req.send();
        Assert.assertTrue(res.isSuccessful());        
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("User added.", n.path("message").asText());        
    }

    @Test(groups = "usersVerify", dependsOnGroups = "users")
    public void verifyUserTom() throws Exception {
        PostmanRequest req = collection.getRequestByName("Check User Tom");
        Response res = req.send();
        Assert.assertTrue(res.isSuccessful());        
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("tom", n.path("username").asText());
        Assert.assertEquals("Tom R. Fields", n.path("nickname").asText());
        Assert.assertEquals("tom@muckdragon.info", n.path("email").asText());
    }    
    
    @Test(groups = "usersVerify", dependsOnGroups = "users")
    public void verifyUserLarry() throws Exception {
        PostmanRequest req = collection.getRequestByName("Check User Larry");
        Response res = req.send();
        Assert.assertTrue(res.isSuccessful());        
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("larry", n.path("username").asText());        
        Assert.assertEquals("Larry Wall", n.path("nickname").asText());
        Assert.assertTrue(n.path("email").isMissingNode());
    }
    
    @Test(groups = "posts", dependsOnGroups = "users")
    public void createPostTom() throws Exception {
        PostmanRequest req = collection.getRequestByName("Tom Posts Message");
        Response res = req.send();
        Assert.assertTrue(res.isSuccessful());
    }
    
    @Test(groups = "posts", dependsOnGroups = "users")
    public void createPostLarry() throws Exception {
        PostmanRequest req = collection.getRequestByName("Lots of Hash Tags");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
    }
    
    @Test(groups = "postsVerify", dependsOnGroups = "posts")
    public void verifyPostTom() throws Exception {
        PostmanRequest req = collection.getRequestByName("Check Tom's Posts");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        Assert.assertTrue(n.path(0).path("tags").isArray());
        Assert.assertEquals("This is an #example of a post which has a lot of those pointless #pound signs.", n.path(0).path("text").asText());
        RestAssert.assertArrayContainsValue(n.path(0).path("tags"), "example");
        RestAssert.assertArrayContainsValue(n.path(0).path("tags"), "pound");
    }
    
    @Test(groups = "postsVerify", dependsOnGroups = "posts")
    public void verifyPostLarry() throws Exception {
        PostmanRequest req = collection.getRequestByName("Read Larry's Posts");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        Assert.assertTrue(n.path(0).path("tags").isArray());
        Assert.assertEquals("I don't get those #tags anyway. Why #are #they #practically #before #every #word.", n.path(0).path("text").asText());
        RestAssert.assertArrayContainsValues(n.path(0).path("tags"), "tags", "are", "they", "practically", "before", "every", "word");
    }
    
    @Test(groups = "postsVerify", dependsOnGroups = "posts")
    public void verifyPostLarrySingle() throws Exception {
        PostmanRequest req = collection.getRequestByName("Another Way to Read a Post");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        Assert.assertTrue(n.path("tags").isArray());
        Assert.assertEquals("I don't get those #tags anyway. Why #are #they #practically #before #every #word.", n.path("text").asText());
        RestAssert.assertArrayContainsValues(n.path("tags"), "tags", "are", "they", "practically", "before", "every", "word");   
    }
    
    @Test(groups = "friends", dependsOnGroups = "users")
    public void addFriendTomLarry() throws Exception {
        PostmanRequest req = collection.getRequestByName("Tom Adds Larry");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
    }
    
    @Test(groups = "friends", dependsOnGroups = "users")
    public void addFriendLarryTom() throws Exception {
        PostmanRequest req = collection.getRequestByName("Larry Adds Tom Back");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());        
    }
    
    @Test(groups = "friendsVerify", dependsOnGroups = "friends")
    public void verifyFriendTomLarry() throws Exception {
        PostmanRequest req = collection.getRequestByName("Check User Tom");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        Assert.assertTrue(n.path("friends").isArray());
        RestAssert.assertArrayContainsValue(n.path("friends"), "larry");
    }
    
    @Test(groups = "friendsVerify", dependsOnGroups = "friends")
    public void verifyFriendLarryTom() throws Exception {
        PostmanRequest req = collection.getRequestByName("Check User Larry");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        Assert.assertTrue(n.path("friends").isArray());
        RestAssert.assertArrayContainsValue(n.path("friends"), "tom");
    }
    
    @Test(groups = "friendsRemove", dependsOnGroups = "friendsVerify")
    public void removeFriendLarryTom() throws Exception {
        PostmanRequest req = collection.getRequestByName("Larry Removes Tom");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());        
    }
    
    @Test(groups = "friendsRemoveVerify", dependsOnGroups = "friendsRemove")
    public void verifyRemoveFriendLarryTom() throws Exception {
        PostmanRequest req = collection.getRequestByName("Check User Larry");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        Assert.assertTrue(n.path("friends").isArray());
        RestAssert.assertArrayNotContainsValue(n.path("friends"), "tom");        
    }
    
    @Test(groups = "patchEmail", dependsOnGroups = "usersVerify")
    public void patchLarryEmail() throws Exception {
        PostmanRequest req = collection.getRequestByName("Larry Adds Email Address");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
    }
    
    @Test(groups = "patchEmailVerify", dependsOnGroups = "patchEmail")
    public void verifyPatchEmail() throws Exception {
        PostmanRequest req = collection.getRequestByName("Check User Larry");
        Response res = req.send();        
        Assert.assertTrue(res.isSuccessful());
        JsonNode n = map.readTree(res.body().string());
        Assert.assertEquals("larry@wall.org", n.path("email").asText());        
    }    
    
}
