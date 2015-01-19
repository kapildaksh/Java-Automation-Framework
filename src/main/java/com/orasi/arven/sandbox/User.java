/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.arven.sandbox;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Set;

/**
 * Simple User POJO for the MicroBlog server. 
 * 
 * @author Brian Becker
 */
@JsonIgnoreProperties({"posts"})
class User {   
    public String username;
    public String nickname;
    public String email;
    public List<Post> posts;
    
    @JsonSerialize(using = SerializeToString.class)
    public Set<User> friends;
        
    @Override
    public String toString() { return this.username; }
    
    @JsonCreator
    public static User createUser(String name) {
        return MapFactory.getUsers().get(name);
    }
    
    public User() { }
}
