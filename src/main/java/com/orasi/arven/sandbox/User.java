/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.arven.sandbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orasi.utils.types.Reference;
import java.util.List;
import java.util.Set;

/**
 * Simple User POJO for the MicroBlog server. 
 * 
 * @author Brian Becker
 */
@JsonIgnoreProperties({"posts"})
public class User {   
    public String username;
    public String nickname;
    public String email;
    public List<Post> posts;
    
    public Set<Reference<User>> friends;
}
