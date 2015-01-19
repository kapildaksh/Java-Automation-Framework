/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.arven.sandbox;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author brian.becker
 */
public class MapFactory {
    private static Map<String, User> users;
    
    public static Map<String, User> getUsers() { 
        if(users == null) {
            users = new HashMap<String, User>();
        }
        return users;
    }
}
