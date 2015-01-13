/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest.api;

/**
 * This message represents some basic CRUD operation has completed;
 * 
 * @author Brian Becker
 */
public class BasicMessage {
    public static enum RequestType { CREATE, READ, UPDATE, DELETE, PROCESS };

    private final int code;
    public int getErrorCode() { return code; }
    
    private final RequestType type;
    public RequestType getType() { return type; }
    
    private final String message;
    public String getMessage() { return message; }
    
    public BasicMessage(int code, RequestType type, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
    }
}
