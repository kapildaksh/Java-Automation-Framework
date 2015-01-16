package com.orasi.arven.sandbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.rest.Patch;
import java.io.PrintWriter;
import java.io.StringWriter;
import static sparkfive.Spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sparkfive.ExceptionHandler;

import sparkfive.Request;
import sparkfive.Response;
import sparkfive.ResponseTransformer;
import sparkfive.Route;

public class MockMicroblogServer {

    public static class JsonTransformer implements ResponseTransformer {

        private final ObjectMapper om = new ObjectMapper();

        @Override
        public String render(Object model) {
            try {
                if(model != null) {
                    return om.writeValueAsString(model);
                }
            } catch (Exception e) { }
            return "Resource not found.";
        }

    }
    
    private static final Map<String, User> users = new HashMap<String, User>();
    private static final Map<String, List<String>> comments = new HashMap<String, List<String>>();
    
    private static final ObjectMapper map = new ObjectMapper();
    
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
                return users.get(req.params(":name"));
            }
        }, new JsonTransformer());
        
        patch("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                User nu = Patch.patch(req.body(), users.remove(req.params(":name")));
                users.put(nu.username, nu);
                return nu.username;                    
            }
        });
        
        delete("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("text/plain; charset=UTF-8");
                return users.remove(req.params(":name")) == null ? "Nothing removed" : "User removed";
            }
        });        
        
        exception(Exception.class, new ExceptionHandler() {
            @Override
            public void handle(Exception exception, Request request, Response response) {
                response.status(500);
                response.type("text/plain; charset=UTF-8");
                StringWriter str = new StringWriter();
                PrintWriter sw = new PrintWriter(str);
                sw.append("Something went horribly wrong.\n\n");
                exception.printStackTrace(sw);
                response.body(str.toString());
            }
        });

    }
}