package com.orasi.arven.sandbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.rest.Json;
import com.orasi.utils.rest.Patch;
import static sparkfive.Spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sparkfive.Request;
import sparkfive.Response;
import sparkfive.Route;

public class MockMicroblogServer {
    private static final Map<String, User> users = new HashMap<String, User>();
    private static final Map<String, List<String>> comments = new HashMap<String, List<String>>();
    
    private static ObjectMapper map = new ObjectMapper();
    
    public static void main( final String[] args ) throws Exception {
        
        post("/users", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                User u = map.readValue(req.body(), User.class);
                users.put(u.username, u);
                return u.username;
            }
        });
                
        get("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                return map.writeValueAsString(users.get(req.params(":name")));
            }
        });
        
        patch("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                User nu = Json.patch(req.body(), users.remove(req.params(":name")), User.class);
                users.put(nu.username, nu);
                return nu.username;
            }
        });
        
        delete("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                return users.remove(req.params(":name"));
            }
        });        
    }
}