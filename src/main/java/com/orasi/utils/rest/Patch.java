/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        public final JsonNode value;
               
        @JsonCreator
        public PatchEntry(@JsonProperty("op") Op op, @JsonProperty("from") String from, @JsonProperty("path") String path, @JsonProperty("value") Object value) {
            if(!(value instanceof JsonNode)) {
                ObjectMapper map = new ObjectMapper();
                this.value = map.valueToTree(value);
            } else {
                this.value = (JsonNode) value;
            }
            
            this.op = op; this.path = path; this.from = from;
        }
        
        public void remove(JsonNode node, String element) {
            if(node instanceof ObjectNode) {
                ((ObjectNode)node).remove(element);
            } else if(node instanceof ArrayNode) {
                ((ArrayNode)node).remove(Integer.valueOf(element));
            }
        }
        
        public void replace(JsonNode node, String element, JsonNode value) {
            if(node instanceof ObjectNode) {
                ((ObjectNode)node).set(element, value);
            } else if(node instanceof ArrayNode) {
                ((ArrayNode)node).set(Integer.valueOf(element), value);
            }
        }
        
        public void add(JsonNode node, String element, JsonNode value) {
            if(node instanceof ObjectNode) {
                ((ObjectNode)node).set(element, value);
            } else if(node instanceof ArrayNode) {
                ((ArrayNode)node).insert(Integer.valueOf(element), value);
            }
        }
        
        public JsonNode apply(JsonNode node) throws Exception {
            String newpath = path.substring(0, path.lastIndexOf("/"));
            String element = path.substring(path.lastIndexOf("/"));
            String from_newpath = "";
            String from_element = "";
            
            if(from != null) {
                from_newpath = from.substring(0, from.lastIndexOf("/"));
                from_element = from.substring(from.lastIndexOf("/"));
            }
            
            JsonNode newnode = node.at(newpath);
            JsonNode newfrom = node.at(from_newpath);
            
            switch(op) {
                case TEST:
                    if(!newnode.at(element).equals(value))
                        return null;
                    break;
                case REMOVE:
                    remove(newnode, element.substring(1));
                    break;
                case ADD:
                    add(newnode, element.substring(1), value);
                    break;
                case REPLACE:
                    replace(newnode, element.substring(1), value);
                    break;
                case MOVE:
                    add(newnode, element.substring(1), newfrom.at(from_element));
                    remove(newfrom, from_element.substring(1));
                    break;
                case COPY:
                    add(newnode, element.substring(1), newfrom.at(from_element));
                    break;
            }
            return node;
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
    
    public String apply(String json) {
        try {
            return map.writeValueAsString(apply(map.readTree(json)));
        } catch (Exception e) {}
        return json;
    }
    
    public JsonNode apply(JsonNode node) {
        for(PatchEntry pe : entries) {
            try {
                if(pe.apply(node) == null)
                    return null;
            } catch (Exception ex) {
                Logger.getLogger(Patch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return node;
    }
    
    public <T> T apply(T o) {
        try {
            JsonNode n = this.map.valueToTree(o);
            this.apply(n);
            return (T) map.treeToValue(n, o.getClass());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Patch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o;
    }
    
    public static <T> T patch(String json, Object o) {
        ObjectMapper m = new ObjectMapper();
        m.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        try {
            Patch p = new Patch((List<Patch.PatchEntry>)m.readValue(json, new TypeReference<List<Patch.PatchEntry>>() { }));
            return (T) p.apply(o);
        } catch (IOException ex) {
            Logger.getLogger(Patch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) o;
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
