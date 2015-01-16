/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.arven.sandbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

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
}
