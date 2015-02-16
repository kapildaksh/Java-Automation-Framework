/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple User POJO for the MicroBlog server. 
 * 
 * @author Brian Becker
 */
@XmlRootElement
public class User {   

    public String username;
    public String nickname;
    public String email;
    public String password;
    
}
