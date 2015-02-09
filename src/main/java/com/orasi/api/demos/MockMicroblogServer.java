package com.orasi.api.demos;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.google.common.collect.Lists;

import com.orasi.api.demos.Message.Type;
import com.orasi.utils.rest.Json;
import com.orasi.utils.rest.Patch;
import com.orasi.utils.types.Name;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.StringUtils;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.ssl.Base64;

import sparkfive.ExceptionHandler;
import sparkfive.Filter;
import sparkfive.Request;
import sparkfive.Response;
import sparkfive.ResponseTransformer;
import sparkfive.Route;
import sparkfive.Session;
import sparkfive.SparkInstance;

public class MockMicroblogServer {
    
    private boolean running = false;
    
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
            String str;
            
            if(model instanceof ModelMutator) {
                model = ((ModelMutator)model).model();
            }
            
            if(model != null) {
                return om.writeValueAsString(model);
            }
            return om.writeValueAsString(new Message(Type.ERROR, "Resource not found."));
        }

    }
    
    private static final Map<String, Name<User>> users = new HashMap<String, Name<User>>();
    private static final MultiMap tagdir = new MultiValueMap();
    private static final MultiMap groups = new MultiValueMap();
    private static final MultiMap exclusions = new MultiValueMap();
    private static final Map creator = new HashMap();
       
    private final SparkInstance srv;
    
    public MockMicroblogServer() {
        this.srv = new SparkInstance();
    }
    
    public void stop() {
        srv.stop();
        running = false;
    }
    
    public boolean isRunning() {
        return running;
    }
    
    public void clear() {
        groups.clear();
        tagdir.clear();
        users.clear();
        creator.clear();
        exclusions.clear();
    }
    
    public void start() {
        running = true;
                
        srv.before("/users/*", new Filter() {
            @Override
            public void handle(Request req, Response res) throws Exception {
                if(req.session().attribute("user") == null) {
                    srv.halt(401, "Unauthorized");
                }
            }
        });
        
        srv.get("/version", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                response.type("text/plain; charset=UTF-8");
                return "v1.0";
            }
        });
        
        srv.post("/login", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("text/plain; charset=UTF-8");
                Session s = req.session();
                if(req.headers("Authorization") != null) {
                    String auth = req.headers("Authorization").split(" ")[1];
                    auth = StringUtils.newStringUtf8(Base64.decodeBase64(auth));
                    String creds[] = auth.split(":");
                    if(!Name.get(users.get(creds[0])).password.equals(creds[1])) {
                        srv.halt(401, "Invalid authorization.");
                    } else {
                        s.attribute("user", creds[0]);
                    }
                }
                if(s.attribute("user") == null){
                    res.header("WWW-Authenticate", "Basic realm=\"MicroblogServer\"");
                    srv.halt(401, "Authorization required.");
                }
                return "Login Successful";
            }
        });        
        
        srv.post("/logout", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                request.session().attribute("user", null);
                return "Logout Successful";
            }
        });
               
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
        
        srv.post("/groups", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                String user = req.session().attribute("user");
                User u = Name.get(users.get(user));
                if(user == null) {
                    srv.halt(401, "Unauthorized");
                }                
                res.type("application/json; charset=UTF-8");
                JsonNode n = Json.Map.readTree(req.body());
                String name = n.path("name").asText();
                if(groups.containsKey(name)) {
                    res.status(409);
                    return new Message(Type.ERROR, "Group already exists.");
                }
                groups.put(name, req.session().attribute("user"));
                creator.put(name, req.session().attribute("user"));
                u.groups.add(name);
                return new Message(Type.INFORMATIONAL, "Group added.");
            }
        }, new JsonTransformer());
        
        srv.post("/groups/:name/join", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                String user = req.session().attribute("user");
                User u = Name.get(users.get(user));
                if(user == null) {
                    srv.halt(401, "Unauthorized");
                }
                Collection c = (Collection) groups.get(req.params(":name"));
                if(c.contains(user)) {
                    res.status(409);
                    return new Message(Type.ERROR, "Group already joined.");
                }
                for(String group : u.groups) {
                    if(exclusions.containsKey(Pair.of(req.params(":name"), group))) {
                        res.status(409);
                        return new Message(Type.ERROR, "Your group " + group + " is in mutual exclusion with this group " + req.params(":name"));
                    }
                }
                u.groups.add(req.params(":name"));
                groups.put(req.params(":name"), user);
                return new Message(Type.INFORMATIONAL, "Group joined.");
            }
        }, new JsonTransformer());
        
        srv.post("/groups/:name/leave", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                String user = req.session().attribute("user");
                User u = Name.get(users.get(user));
                if(user == null) {
                    srv.halt(401, "Unauthorized");
                }
                Collection c = (Collection) groups.get(req.params(":name"));
                if(!c.contains(user)) {
                    res.status(409);
                    return new Message(Type.ERROR, "Not a member of the group.");
                }
                Name.get(users.get(user)).groups.add(req.params(":name"));
                groups.remove(req.params(":name"), req.session().attribute("user"));
                u.groups.remove(req.params(":name"));
                return new Message(Type.INFORMATIONAL, "Group left.");
            }
        }, new JsonTransformer());
        
        srv.put("/groups/:name/exclusion/:exclusion", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                if(req.session().attribute("user") == null) {
                    srv.halt(401, "Unauthorized");
                } else if(!req.session().attribute("user").equals(creator.get(req.params(":name")))) {
                    srv.halt(403, "Forbidden " + req.session().attribute("user") + " " + req.params(":name"));
                }
                exclusions.put(Pair.of(req.params(":name"), req.params(":exclusion")), req.params(":name"));
                exclusions.put(Pair.of(req.params(":exclusion"), req.params(":name")), req.params(":name"));
                return new Message(Type.INFORMATIONAL, "Exclusion set.");
            }
        }, new JsonTransformer());
        
        srv.delete("/groups/:name/exclusion/:exclusion", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                if(req.session().attribute("user") == null) {
                    srv.halt(401, "Unauthorized");
                } else if(!req.session().attribute("user").equals(creator.get(req.params(":name")))) {
                    srv.halt(403, "Forbidden");
                }
                exclusions.remove(Pair.of(req.params(":name"), req.params(":exclusion")), req.params(":name"));
                exclusions.remove(Pair.of(req.params(":exclusion"), req.params(":name")), req.params(":name"));
                return new Message(Type.INFORMATIONAL, "Exclusion removed.");
            }
        }, new JsonTransformer());        
        
        srv.get("/groups/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");    
                if( groups.get(req.params(":name")) == null ) {
                    res.status(404);
                }                
                return groups.get(req.params(":name"));
            }
        }, new JsonTransformer());
        
        srv.post("/users", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                User u = Json.Map.readValue(req.body(), User.class);
                if(users.containsKey(u.username)) {
                    res.status(409);
                    return new Message(Type.ERROR, "User already exists.");
                }
                users.put(u.username, new Name<User>(u));
                return new Message(Type.INFORMATIONAL, "User added.");
            }
        }, new JsonTransformer());        
                
        srv.get("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                res.type("application/json; charset=UTF-8");
                Name<User> user = users.get(req.params(":name"));
                return Name.get(user);
            }
        }, new JsonTransformer());
        
        srv.patch("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                if(!req.params(":name").equals(req.session().attribute("user"))) {
                    srv.halt(403, "Forbidden");
                }
                res.type("application/json; charset=UTF-8");
                Name<User> u = users.get(req.params(":name"));
                u.set((User) Patch.patch(req.body(), Name.get(u)));
                return new Message(Type.INFORMATIONAL, "New Username: ".concat(Name.get(u).username));                    
            }
        }, new JsonTransformer());
        
        srv.delete("/users/:name", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                if(!req.params(":name").equals(req.session().attribute("user"))) {
                    srv.halt(403, "Forbidden");
                }
                res.type("application/json; charset=UTF-8");
                return Name.isNull(users.remove(req.params(":name"))) ? new Message(Type.ERROR, "Could not find user.") : new Message(Type.INFORMATIONAL, "User removed.");
            }
        }, new JsonTransformer());
        
        srv.put("/users/:name/friends/:friend", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                if(!req.params(":name").equals(req.session().attribute("user"))) {
                    srv.halt(403, "Forbidden");
                }
                res.type("application/json; charset=UTF-8");
                String fn = req.params(":friend");
                String mn = req.params(":name");
                if(users.containsKey(mn) && users.containsKey(fn)) {
                    Name<User> u = users.get(mn);
                    Name<User> u2 = users.get(fn);
                    if(Name.get(u).friends == null)
                        Name.get(u).friends = new HashSet();
                    Name.get(u).friends.add(u2);
                    return new Message(Type.INFORMATIONAL, "Added friend to set.");
                }
                return new Message(Type.ERROR, "Could not find user or friend.");
            }
        }, new JsonTransformer());
        
        srv.delete("/users/:name/friends/:friend", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                if(!req.params(":name").equals(req.session().attribute("user"))) {
                    srv.halt(403, "Forbidden");
                }
                res.type("application/json; charset=UTF-8");
                String fn = req.params(":friend");
                String mn = req.params(":name");
                if(users.containsKey(mn) && users.containsKey(fn)) {
                    Name<User> u = users.get(mn);
                    Name<User> u2 = users.get(fn);
                    if(Name.get(u).friends.contains(u2)) {
                        Name.get(u).friends.remove(u2);
                        return new Message(Type.INFORMATIONAL, "Friend removed.");
                    }
                }
                return new Message(Type.ERROR, "Could not find user or friend.");
            }
        }, new JsonTransformer());        
        
        srv.post("/users/:name/posts", new Route() {
            @Override
            public Object handle(Request req, Response res) throws Exception {
                if(!req.params(":name").equals(req.session().attribute("user"))) {
                    srv.halt(403, "Forbidden");
                }
                res.type("application/json; charset=UTF-8");
                Post p = Json.Map.readValue(req.body(), Post.class);
                p.created = new Date();
                if(users.containsKey(req.params(":name"))) {
                    Name<User> u = users.get(req.params(":name"));
                    if(Name.get(u).posts == null) {
                        Name.get(u).posts = new ArrayList<Post>();
                    }
                    p.id = Name.get(u).posts.size();
                    Name.get(u).posts.add(p);
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
                    Name<User> u = users.get(req.params(":name"));
                    if(Name.get(u).posts == null) {
                        Name.get(u).posts = new ArrayList<Post>();
                    }
                    return new ArrayOffset(req.queryParams("offset"), 20, Lists.reverse(Name.get(u).posts));
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
                    Name<User> u = users.get(req.params(":name"));
                    int number = Integer.valueOf(req.params(":number"));
                    if(number < Name.get(u).posts.size()) {
                        return Name.get(u).posts.get(number);
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