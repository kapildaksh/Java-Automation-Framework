/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Brian Becker
 */
@Service
public class MicroblogService {
    
    public Map<String, User> users;
    
    public MicroblogService() {
        users = new HashMap<String, User>();
    }
    
    public User getUser( String user ) {
        if(!users.containsKey(user))
            throw new NotFoundException();
        return users.get(user);
    }

    public void addUser( User user ) {
        users.put(user.username, user);
    }
    
}
