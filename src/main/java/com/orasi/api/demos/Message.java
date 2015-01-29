/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.demos;

/**
 *
 * @author brian.becker
 */
public class Message {
    public enum Type {
        ERROR, DEBUGGING, INFORMATIONAL
    }
    
    public Type type;
    public String message;
    
    public Message(Type type, String message) {
        this.type = type;
        this.message = message;
    }
}
