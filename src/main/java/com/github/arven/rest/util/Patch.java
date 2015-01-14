/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.LinkedList;
import java.util.List;

/**
 * HTTP Patch Request (JSON-PATCH format)
 * 
 * @author Brian Becker
 */
public class Patch {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
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
        
        public boolean apply(JsonNode node) {
            switch(op) {
                case TEST:
                    if(!node.at(path).equals(value))
                        return false;
                    break;
                case REMOVE:
                    break;
                case ADD:
                    break;
                case REPLACE:
                    break;
                case MOVE:
                    break;
                case COPY:
                    break;
            }
            return true;
        }
    }
    
    /**
     * Builder for HTTP Patch Request (JSON-PATCH format)
     * 
     * @author Brian Becker
     */
    public static class Builder {
        private final List<PatchEntry> entries = new LinkedList<PatchEntry>();
        
        public Builder test(String path, Object value) {
            this.entries.add(new PatchEntry(Op.TEST, null, path, value));
            return this;
        }

        public Builder add(String path, Object value) {
            this.entries.add(new PatchEntry(Op.ADD, null, path, value));
            return this;
        }

        public Builder replace(String path, Object value) {
            this.entries.add(new PatchEntry(Op.REPLACE, null, path, value));
            return this;
        }
        
        public Builder move(String from, String path) {
            this.entries.add(new PatchEntry(Op.MOVE, from, path, null));
            return this;
        }
        
        public Builder copy(String from, String path) {
            this.entries.add(new PatchEntry(Op.COPY, from, path, null));
            return this;
        }

        public Builder remove(String path) {
            this.entries.add(new PatchEntry(Op.REMOVE, null, path, null));
            return this;
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
        this.map.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
    }
    
    public void apply(JsonNode node) {
        for(PatchEntry pe : entries) {
            pe.apply(node);
        }
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
