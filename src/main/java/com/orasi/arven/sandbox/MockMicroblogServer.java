package com.orasi.arven.sandbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.orasi.arven.sandbox.Message.Type;
import com.orasi.utils.rest.Patch;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import static sparkfive.Spark.*;

import java.util.HashMap;
import java.util.Map;
import sparkfive.ExceptionHandler;

import sparkfive.Request;
import sparkfive.Response;
import sparkfive.ResponseTransformer;
import sparkfive.Route;

public class MockMicroblogServer {

    public static class JsonTransformer implements ResponseTransformer {

        private final ObjectMapper om = new ObjectMapper();

        public JsonTransformer() {
            om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        }
        
        @Override
        public String render(Object model) throws Exception {
            try {
                if(model != null) {
                    return om.writeValueAsString(model);
                }
            } catch(Exception e) {}
            return om.writeValueAsString(new Message(Type.ERROR, "Resource not found."));
        }

    }
    
    private static final Map<String, User> users = new HashMap<String, User>();
    
    private static final ObjectMapper map = new ObjectMapper();
    
    public static void main( final String[] args ) throws Exception {
        
        post("/users", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                User u = map.readValue(req.body(), User.class);
                users.put(u.username, u);
                return new Message(Type.INFORMATIONAL, "User added.");
            }
        }, new JsonTransformer());
                
        get("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                return users.get(req.params(":name"));
            }
        }, new JsonTransformer());
        
        patch("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");                
                User nu = Patch.patch(req.body(), users.remove(req.params(":name")));
                users.put(nu.username, nu);
                return new Message(Type.INFORMATIONAL, "New Username: ".concat(nu.username));                    
            }
        }, new JsonTransformer());
        
        delete("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                return users.remove(req.params(":name")) == null ? new Message(Type.ERROR, "Could not find user.") : new Message(Type.INFORMATIONAL, "User removed.");
            }
        }, new JsonTransformer());
        
        post("/users/:name/posts", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                Post p = map.readValue(req.body(), Post.class);
                p.created = new Date();
                if(users.containsKey(req.params(":name"))) {
                    if(users.get(req.params(":name")).posts == null) {
                        users.get(req.params(":name")).posts = new ArrayList<Post>();
                    }
                    p.id = users.get(req.params(":name")).posts.size();
                    users.get(req.params(":name")).posts.add(p);
                    return new Message(Type.INFORMATIONAL, "Message posted.");
                }
                res.status(500);
                return new Message(Type.ERROR, "Could not post message.");
            }
        }, new JsonTransformer());
        
        get("/users/:name/posts", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                if(users.containsKey(req.params(":name"))) {
                    User u = users.get(req.params(":name"));
                    if(u.posts == null) {
                        u.posts = new ArrayList<Post>();
                    }
                    int offset = 0;
                    if(req.queryParams("offset") != null) {
                        offset = Integer.valueOf(req.queryParams("offset"));
                    }
                    int end = u.posts.size() < (offset + 20) ? u.posts.size() : offset + 20;
                    return offset <= end ? u.posts.subList(offset, end) : Collections.EMPTY_LIST;
                }
                res.status(404);
                return new Message(Type.ERROR, "Could not find user.");
            }
        }, new JsonTransformer());
        
        get("/users/:name/posts/:number", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                if(users.containsKey(req.params(":name"))) {
                    User u = users.get(req.params(":name"));
                    int number = Integer.valueOf(req.params(":number"));
                    if(number < u.posts.size()) {
                        return u.posts.get(number);
                    } else {
                        return new Message(Type.ERROR, "Could not find post.");
                    }
                }
                return new Message(Type.ERROR, "Could not find user.");
            }
        }, new JsonTransformer());
        
        delete("/users/:name/posts/:number", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                return users.remove(req.params(":name")) == null ? new Message(Type.ERROR, "Could not find user.") : new Message(Type.INFORMATIONAL, "User removed.");
            }
        }, new JsonTransformer());
        
        exception(Exception.class, new ExceptionHandler() {
            @Override
            public void handle(Exception exception, Request request, Response response) {
                response.status(500);
                response.type("text/plain; charset=UTF-8");
                StringWriter str = new StringWriter();
                PrintWriter sw = new PrintWriter(str);
                sw.append("Something went horribly wrong. Here's the stack trace. \n\n");
                exception.printStackTrace(sw);
                response.body(str.toString());
            }
        });

    }
}