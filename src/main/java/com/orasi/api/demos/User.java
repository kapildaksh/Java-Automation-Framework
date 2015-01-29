/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.demos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orasi.utils.types.Reference;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Simple User POJO for the MicroBlog server. 
 * 
 * @author Brian Becker
 */
@JsonIgnoreProperties({"posts"})
public class User {   
    @JsonIgnore
    public String password;
    
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String username;
    public String nickname;
    public String email;
    public List<Post> posts;
            
    @JsonIgnore
    public List<String> groups = new LinkedList<String>();
    
    @JsonProperty
    public List<String> getGroups() {
        return this.groups;
    }
    
    public Set<Reference<User>> friends;
    
    @Override
    public String toString() {
        return this.username;
    }
}
