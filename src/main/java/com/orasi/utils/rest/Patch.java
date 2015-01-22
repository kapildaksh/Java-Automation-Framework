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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * HTTP Patch Request (JSON-PATCH format)
 * 
 * This Patch class is designed to allow for patching REST requests and
 * responses as well as generic JSON data. This is useful for when you
 * must store up a list of operations on a JsonNode because you do not
 * have access to the node yet.
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
                this.value = Json.MAP.valueToTree(value);
            } else {
                this.value = (JsonNode) value;
            }
            
            this.op = op; this.path = path; this.from = from;
        }
        
        private void remove(JsonNode node, String element) {
            if(node instanceof ObjectNode) {
                ((ObjectNode)node).remove(element);
            } else if(node instanceof ArrayNode) {
                ((ArrayNode)node).remove(Integer.valueOf(element));
            }
        }
        
        private void replace(JsonNode node, String element, JsonNode value) {
            if(node instanceof ObjectNode) {
                ((ObjectNode)node).set(element, value);
            } else if(node instanceof ArrayNode) {
                ((ArrayNode)node).set(Integer.valueOf(element), value);
            }
        }
        
        private void add(JsonNode node, String element, JsonNode value) {
            if(node instanceof ObjectNode) {
                ((ObjectNode)node).set(element, value);
            } else if(node instanceof ArrayNode) {
                ((ArrayNode)node).insert(Integer.valueOf(element), value);
            }
        }
        
        private JsonNode apply(JsonNode node) throws Exception {
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
        
        /**
         * Create a JSON Patch test node. This determines if the given path
         * contains the given value.
         * 
         * @param path location to test
         * @param value value to ensure location matches
         * @return this
         */
        public Builder test(String path, Object value) {
            this.entries.add(new PatchEntry(Op.TEST, null, path, value));
            return this;
        }

        /**
         * Create a JSON Patch add node. This adds a value or a structure at
         * the given path.
         * 
         * @param path location of node to be appended to
         * @param value value to append to node
         * @return this
         */
        public Builder add(String path, Object value) {
            this.entries.add(new PatchEntry(Op.ADD, null, path, value));
            return this;
        }

        /**
         * Create a JSON Patch replace node. This replaces a value or structure
         * at a given path with a given value.
         * 
         * @param path location of node to be replaced
         * @param value value to replace node with
         * @return this
         */
        public Builder replace(String path, Object value) {
            this.entries.add(new PatchEntry(Op.REPLACE, null, path, value));
            return this;
        }
        
        /**
         * Create a JSON Patch move node. This replaces a value or structure
         * at a given path with another given path, and removes the from
         * location.
         * 
         * @param from location of source node (to be removed)
         * @param path location of destination node
         * @return this
         */
        public Builder move(String from, String path) {
            this.entries.add(new PatchEntry(Op.MOVE, from, path, null));
            return this;
        }
        
        /**
         * Create a JSON Patch copy node. This adds a value or structure at
         * a given path with another given path, leaving the original intact.
         * 
         * @param from location of source node
         * @param path location of destination node
         * @return this
         */
        public Builder copy(String from, String path) {
            this.entries.add(new PatchEntry(Op.COPY, from, path, null));
            return this;
        }

        /**
         * Create a JSON Patch remove node. This removes a value or structure
         * at a given path.
         * 
         * @param path location of node to be removed
         * @return this
         */
        public Builder remove(String path) {
            this.entries.add(new PatchEntry(Op.REMOVE, null, path, null));
            return this;
        }

        /**
         * Build a JSON Patch, which can be applied to a string value that is
         * valid JSON or can be applied to a JsonNode itself.
         * 
         * @return Patch object
         */
        public Patch build() {
            return new Patch(this.entries);
        }
    }
    
    private final List<PatchEntry> entries;
    
    public Patch(List<PatchEntry> entries) {
        this.entries = entries;
    }
    
    public Patch() {
        this.entries = new LinkedList<PatchEntry>();
    }
    
    /**
     * Apply this patch to a string, returning another string which is the
     * string with the patch applied. If the patch failed to apply, the
     * string will be unchanged.
     * 
     * @param json the string to evaluate
     * @return 
     */
    public String apply(String json) {
        try {
            return Json.MAP.writeValueAsString(apply(Json.MAP.readTree(json)));
        } catch (Exception e) {}
        return json;
    }
    
    /**
     * Apply this patch to a JsonNode, returning the same node which is the
     * node with the patch applied (and the existing node is not preserved). If
     * the patch failed to apply, the node will be unchanged.
     * 
     * @param node the node to evaluate
     * @return 
     */
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
    
    /**
     * Apply this patch to a Java Object, returning the same object which is
     * the object with the patch applied (and the existing object is not
     * preserved). If the patch failed to apply, the object will be unchanged.
     * 
     * @param <T> type of java object
     * @param o java object
     * @return modified object
     */
    public <T> T apply(T o) {
        try {
            JsonNode n = Json.MAP.valueToTree(o);
            this.apply(n);
            return (T) Json.MAP.treeToValue(n, o.getClass());
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Patch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o;
    }
    
    /**
     * Apply a patch which is stored in a string format, first using the string
     * to create a Patch object, and then applying the patch to the passed
     * object.
     * 
     * @param <T> type of java object
     * @param json the patch, as a string
     * @param o the object to modify
     * @return the modified object
     */
    public static <T> T patch(String json, Object o) {
        try {
            Patch p = new Patch((List<Patch.PatchEntry>)Json.MAP.readValue(json, new TypeReference<List<Patch.PatchEntry>>() { }));
            return (T) p.apply(o);
        } catch (IOException ex) {
            Logger.getLogger(Patch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) o;
    }    
    
    @Override
    public String toString() {
        try {
            return Json.MAP.writeValueAsString(this.entries);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace(System.out);
            return "[]";
        }
    }
    
    public static Patch add(Collection<Patch> patches) {
        List<PatchEntry> entries = new LinkedList<PatchEntry>();
        for(Patch p : patches) {
            entries.addAll(p.entries);
        }        
        return new Patch(entries);
    }
    
    public Patch add(Patch patch) {
        this.entries.addAll(patch.entries);
        return this;
    }
}
