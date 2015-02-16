/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.springframework.stereotype.Service;

/**
 * @author Brian Becker
 */
@Service
public class MicroblogService {
    
    public Map<String, User> users;
    public MultivaluedMap<String, Post> posts;
    
    public MicroblogService() {
        users = new HashMap<String, User>();
        posts = new MultivaluedHashMap<String, Post>();
    }
    
    public User getUser( String user ) {
        if(!users.containsKey(user))
            throw new NotFoundException();
        return users.get(user);
    }
    
    public List<Post> getPosts( String user ) {
        if(!posts.containsKey(user))
            throw new NotFoundException();
        return posts.get(user);
    }    

    public void addUser( User user ) {
        users.put(user.username, user);
    }
    
    public void addPost( Post post ) {
        posts.add(post.username, post);
    }
    
}
