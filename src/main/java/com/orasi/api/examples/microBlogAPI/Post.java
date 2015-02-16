/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author brian.becker
 */
@XmlRootElement
public class Post {
    
    public String title;
    public String message;
    public String username;
    
}
