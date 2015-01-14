/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedList;
import java.util.List;

/**
 * HTTP Patch Request (JSON-PATCH format)
 * 
 * @author Brian Becker
 */
public class Patch {

    public static enum Op {        
        TEST, REMOVE, ADD, REPLACE, MOVE, COPY;
        
        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
    
    public static class PatchEntry {
        public final Op op;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final String from;
        public final String path;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final Object value;
        
        public PatchEntry(Op op, String from, String path, Object value) {
            this.op = op; this.path = path; this.value = value; this.from = from;
        }
    }
    
    /**
     * Builder for HTTP Patch Request (JSON-PATCH format)
     * 
     * @author Brian Becker
     */
    public static class Builder {
        private final List<PatchEntry> entries = new LinkedList<PatchEntry>();
        
        public void test(String path, Object value) {
            this.entries.add(new PatchEntry(Op.TEST, null, path, value));
        }

        public void add(String path, Object value) {
            this.entries.add(new PatchEntry(Op.ADD, null, path, value));
        }

        public void replace(String path, Object value) {
            this.entries.add(new PatchEntry(Op.REPLACE, null, path, value));
        }
        
        public void move(String from, String path) {
            this.entries.add(new PatchEntry(Op.MOVE, from, path, null));
        }
        
        public void copy(String from, String path) {
            this.entries.add(new PatchEntry(Op.COPY, from, path, null));
        }

        public void remove(String path) {
            this.entries.add(new PatchEntry(Op.REMOVE, null, path, null));
        }

        public Patch build() {
            return new Patch(this.entries);
        }
    }
    
    private final List<PatchEntry> entries;
    private final ObjectMapper map;
    
    public Patch(List<PatchEntry> entries) {
        this.entries = entries;
        this.map = new ObjectMapper();
    }
    
    @Override
    public String toString() {
        try {
            return this.map.writeValueAsString(this.entries);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace(System.out);
            return "[]";
        }
    }
}
