/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest.util;

/**
 *
 * @author Brian Becker
 */
public class PatchBuilder {
    private final StringBuilder builder;
    
    public PatchBuilder() {
        this.builder = new StringBuilder("");
    }
    
    private void next() {
        if(builder.length() != 0) {
            builder.append(",\n");
        }
    }
    
    public void copy(String source, String dest) {
        next();
        builder.append("\t{ \"op\": \"copy\", \"path\": \"").append(source).append("\", \"path\": ").append(dest).append(" }");
    }
    
    public void test(String path, Object value) {
        next();        
        builder.append("\t{ \"op\": \"test\", \"path\": \"").append(path).append("\", \"value\": ").append(value).append(" }");
    }
    
    public void add(String path, Object value) {
        next();        
        builder.append("\t{ \"op\": \"add\", \"path\": \"").append(path).append("\", \"value\": ").append(value).append(" }");
    }
    
    public void replace(String path, Object value) {
        next();        
        builder.append("\t{ \"op\": \"replace\", \"path\": \"").append(path).append("\", \"value\": ").append(value).append(" }");
    }
    
    public void remove(String path) {
        next();        
        builder.append("\t{ \"op\": \"remove\", \"path\": \"").append(path).append("\"");
    }
    
    @Override
    public String toString() {
        return "[\n".concat(builder.toString()).concat("\n]");
    }
}
