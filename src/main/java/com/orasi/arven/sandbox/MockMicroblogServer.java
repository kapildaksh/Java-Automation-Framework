package com.orasi.arven.sandbox;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.google.common.collect.Lists;

import com.orasi.arven.sandbox.Message.Type;
import com.orasi.utils.rest.Patch;
import com.orasi.utils.types.Reference;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.tuple.Pair;

import sparkfive.ExceptionHandler;
import sparkfive.Request;
import sparkfive.Response;
import sparkfive.ResponseTransformer;
import sparkfive.Route;
import sparkfive.SparkInstance;

public class MockMicroblogServer {
    
    public static void main(String[] args) {
        MockMicroblogServer svr = new MockMicroblogServer();
        svr.start();
    }

    public static interface ModelMutator {
        public Object model();
    }
    
    public static class ArrayOffset implements ModelMutator {
        private final String offS;
        private final int size;
        private final List objects;
        
        public ArrayOffset(String offS, int size, List objects) {
            this.offS = offS;
            this.size = size;
            this.objects = objects;
        }
        
        @Override
        public Object model() {
            int offset = 0;
            if(offS != null) {
                offset = Integer.valueOf(offS);
            }
            int end = objects.size() < (offset + 20) ? objects.size() : offset + 20;
            return offset <= end ? objects.subList(offset, end) : Collections.EMPTY_LIST;
        }
    }
    
    public static class JsonTransformer implements ResponseTransformer {

        private final ObjectMapper om = new ObjectMapper();

        public JsonTransformer() {
            om.setSerializationInclusion(Include.NON_EMPTY);
            om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        }
        
        @Override
        public String render(Object model) throws Exception {
            if(model instanceof ModelMutator) {
                model = ((ModelMutator)model).model();
            }
            
            if(model != null) {
                return om.writeValueAsString(model);
            }
            return om.writeValueAsString(new Message(Type.ERROR, "Resource not found."));
        }

    }
    
    private static final Map<String, Reference<User>> users = new HashMap<String, Reference<User>>();
    private static final MultiMap tagdir = new MultiValueMap();
    
    private static final ObjectMapper map = new ObjectMapper();
    
    private final SparkInstance srv;
    
    public MockMicroblogServer() {
        this.srv = new SparkInstance();
    }
    
    public void stop() {
        srv.stop();
    }
    
    public void start() {       
        srv.get("/tags", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                List<Pair<Integer, String>> map = new ArrayList<Pair<Integer, String>>();

                for(Object s : tagdir.keySet()) {
                    List<String> vals = (List<String>) tagdir.get(s);
                    map.add(Pair.of(vals.size(), (String) s));
                }
                
                return map;
            }                        
        }, new JsonTransformer());
        
        srv.get("/tags/:tag", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                if(tagdir.containsKey(req.params(":tag"))) {
                    ArrayList c = (ArrayList) tagdir.get(req.params(":tag"));
                    return new ArrayOffset(req.queryParams("offset"), 20, Lists.reverse(c));
                } else {
                    return Collections.EMPTY_LIST;
                }
            }            
        }, new JsonTransformer());
        
        srv.post("/users", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                User u = map.readValue(req.body(), User.class);
                users.put(u.username, new Reference<User>(u));
                return new Message(Type.INFORMATIONAL, "User added.");
            }
        }, new JsonTransformer());
                
        srv.get("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                Reference<User> user = users.get(req.params(":name"));
                return user.isNull() ? null : user.get();
            }
        }, new JsonTransformer());
        
        srv.patch("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                Reference<User> u = users.get(req.params(":name"));
                u.set((User) Patch.patch(req.body(), u.get()));
                return new Message(Type.INFORMATIONAL, "New Username: ".concat(u.get().username));                    
            }
        }, new JsonTransformer());
        
        srv.delete("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                return users.remove(req.params(":name")).isNull() ? new Message(Type.ERROR, "Could not find user.") : new Message(Type.INFORMATIONAL, "User removed.");
            }
        }, new JsonTransformer());
        
        srv.put("/users/:name/friends/:friend", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                boolean mutual = false;
                String fn = req.params(":friend");
                String mn = req.params(":name");
                if(users.containsKey(mn) && users.containsKey(fn)) {
                    Reference<User> u = users.get(mn);
                    Reference<User> u2 = users.get(fn);
                    if(u.get().friends == null)
                        u.get().friends = new HashSet();
                    u.get().friends.add(u2);
                    return new Message(Type.INFORMATIONAL, "Added friend to set.");
                }
                return new Message(Type.ERROR, "Could not find user or friend.");
            }
        }, new JsonTransformer());
        
        srv.delete("/users/:name/friends/:friend", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                String fn = req.params(":friend");
                String mn = req.params(":name");
                if(users.containsKey(mn) && users.containsKey(fn)) {
                    Reference<User> u = users.get(mn);
                    Reference<User> u2 = users.get(fn);
                    if(u.get().friends.contains(u2)) {
                        u.get().friends.remove(u2);
                        return new Message(Type.INFORMATIONAL, "Friend removed.");
                    }
                }
                return new Message(Type.ERROR, "Could not find user or friend.");
            }
        }, new JsonTransformer());        
        
        srv.post("/users/:name/posts", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                Post p = map.readValue(req.body(), Post.class);
                p.created = new Date();
                if(users.containsKey(req.params(":name"))) {
                    Reference<User> u = users.get(req.params(":name"));
                    if(u.get().posts == null) {
                        u.get().posts = new ArrayList<Post>();
                    }
                    p.id = u.get().posts.size();
                    u.get().posts.add(p);
                    for(String tag : p.getTags()) {
                        tagdir.put(tag, p);
                    }
                    
                    return new Message(Type.INFORMATIONAL, "Message posted.");
                }
                res.status(500);
                return new Message(Type.ERROR, "Could not post message.");
            }
        }, new JsonTransformer());
        
        srv.get("/users/:name/posts", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                if(users.containsKey(req.params(":name"))) {
                    Reference<User> u = users.get(req.params(":name"));
                    if(u.get().posts == null) {
                        u.get().posts = new ArrayList<Post>();
                    }
                    return new ArrayOffset(req.queryParams("offset"), 20, Lists.reverse(u.get().posts));
                }
                res.status(404);
                return new Message(Type.ERROR, "Could not find user.");
            }
        }, new JsonTransformer());
        
        srv.get("/users/:name/posts/:number", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                if(users.containsKey(req.params(":name"))) {
                    Reference<User> u = users.get(req.params(":name"));
                    int number = Integer.valueOf(req.params(":number"));
                    if(number < u.get().posts.size()) {
                        return u.get().posts.get(number);
                    } else {
                        return new Message(Type.ERROR, "Could not find post.");
                    }
                }
                return new Message(Type.ERROR, "Could not find user.");
            }
        }, new JsonTransformer());

        srv.exception(NumberFormatException.class, new ExceptionHandler() {
            @Override
            public void handle(Exception exception, Request request, Response response) {
                response.status(500);
                response.type("text/plain; charset=UTF-8");
                response.body("The value you entered is not a valid number.");
            }
        });        
        
        srv.exception(Exception.class, new ExceptionHandler() {
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