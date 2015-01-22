/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

/**
 *
 * @author brian.becker
 */
public class JsonPointer {
    
    private final String pointer;
    
    public JsonPointer(String pointer) {
        this.pointer = pointer;
    }
           
    public static String fromPath(Object... objs) {
        StringBuilder pointer = new StringBuilder("");
        for(Object o : objs) {
            pointer.append("/").append(o.toString().replace("~", "~0").replace("/", "~1"));
        }
        return pointer.toString();
    }
    
}
