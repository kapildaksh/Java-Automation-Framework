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

    /**
     * An operation on a JSON tree, for use with the Patch Entry type.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    public static enum Op {        
        TEST, REMOVE, ADD, REPLACE, MOVE, COPY;
        
        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
    
    /**
     * The primary building block of the Patch type, this entry is an atomic
     * operation to be performed on a JSON tree.
     */
    public static class PatchEntry {
        public final Op op;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final String from;
        public final String path;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final JsonNode value;
               
        /**
         * A Patch Entry is the primary building block of the Patch type,
         * which contains all of the properties in one element of a Patch.
         * Not all values will be used in patch entries, only the ones
         * which make sense for a given patch type.
         * 
         * @param   op      patching operation (test, remove, add, replace, move, copy)
         * @param   from    source of operation involving another path
         * @param   path    primary path argument
         * @param   value   value to add, insert, etc to location.
         */
        @JsonCreator
        public PatchEntry(@JsonProperty("op") Op op, @JsonProperty("from") String from, @JsonProperty("path") String path, @JsonProperty("value") Object value) {
            if(!(value instanceof JsonNode)) {
                this.value = Json.Map.valueToTree(value);
            } else {
                this.value = (JsonNode) value;
            }
            
            this.op = op; this.path = path; this.from = from;
        }
        
        /**
         * This method removes the element with the given name or index
         * under the specified node.
         * 
         * @param   node    node to erase element from
         * @param   element element to erase
         */
        private void remove(JsonNode node, String element) {
            if(node instanceof ObjectNode) {
                ((ObjectNode)node).remove(element);
            } else if(node instanceof ArrayNode) {
                ((ArrayNode)node).remove(Integer.valueOf(element));
            }
        }
        
        /**
         * This method replaces the node with a value.
         * 
         * @param   node    node to replace value in
         * @param   element ary key or array index
         * @param   value   value to be replaced with
         */
        private void replace(JsonNode node, String element, JsonNode value) {
            if(node instanceof ObjectNode) {
                ((ObjectNode)node).set(element, value);
            } else if(node instanceof ArrayNode) {
                ((ArrayNode)node).set(Integer.valueOf(element), value);
            }
        }
        
        /**
         * This method adds the given value to the given node. If it is an
         * object, then it will instead replace the object if the "element"
         * key is already taken.
         * 
         * @param node      node to add value to
         * @param element   ary key or array index
         * @param value     value to be added
         */
        private void add(JsonNode node, String element, JsonNode value) {
            if(node instanceof ObjectNode) {
                ((ObjectNode)node).set(element, value);
            } else if(node instanceof ArrayNode) {
                ((ArrayNode)node).insert(Integer.valueOf(element), value);
            }
        }
        
        /**
         * 
         * @param   node        node to apply the partial patch to
         * @return
         * @throws  Exception 
         */
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
     * Builder for HTTP Patch Request (JSON-PATCH format). This class allows
     * for the easy construction of patches because all of the defined actions
     * are convenient methods. If one needs to perform actions on a JSON
     * tree which has a defined schema or a specification but the actual tree
     * is not available to the program at the current time, the Patch Builder
     * is useful for performing these actions and then executing them when
     * the JSON tree is available as data.
     * 
     * This method may also be used for the modification of JSON objects which
     * are well-defined when one does not want to deal with the casting between
     * the many different types of JSON nodes. Adding an object will add it
     * in the preferred representation of the global JSON mapper.
     * 
     * @author Brian Becker
     */
    public static class Builder {
        private final List<PatchEntry> entries = new LinkedList<PatchEntry>();
        
        /**
         * Create a JSON Patch test node. This determines if the given path
         * contains the given value.
         * 
         * @param   path    location to test
         * @param   value   value to ensure location matches
         * @return  this
         */
        public Builder test(String path, Object value) {
            this.entries.add(new PatchEntry(Op.TEST, null, path, value));
            return this;
        }

        /**
         * Create a JSON Patch add node. This adds a value or a structure at
         * the given path.
         * 
         * @param   path    location of node to be appended to
         * @param   value   value to append to node
         * @return  this
         */
        public Builder add(String path, Object value) {
            this.entries.add(new PatchEntry(Op.ADD, null, path, value));
            return this;
        }

        /**
         * Create a JSON Patch replace node. This replaces a value or structure
         * at a given path with a given value.
         * 
         * @param   path    location of node to be replaced
         * @param   value   value to replace node with
         * @return  this
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
         * @param   from    location of source node (to be removed)
         * @param   path    location of destination node
         * @return  this
         */
        public Builder move(String from, String path) {
            this.entries.add(new PatchEntry(Op.MOVE, from, path, null));
            return this;
        }
        
        /**
         * Create a JSON Patch copy node. This adds a value or structure at
         * a given path with another given path, leaving the original intact.
         * 
         * @param   from    location of source node
         * @param   path    location of destination node
         * @return  this
         */
        public Builder copy(String from, String path) {
            this.entries.add(new PatchEntry(Op.COPY, from, path, null));
            return this;
        }

        /**
         * Create a JSON Patch remove node. This removes a value or structure
         * at a given path.
         * 
         * @param   path    location of node to be removed
         * @return  this
         */
        public Builder remove(String path) {
            this.entries.add(new PatchEntry(Op.REMOVE, null, path, null));
            return this;
        }

        /**
         * Build a JSON Patch, which can be applied to a string value that is
         * valid JSON or can be applied to a JsonNode itself.
         * 
         * @return  Patch object
         */
        public Patch build() {
            return new Patch(this.entries);
        }
    }
    
    private final List<PatchEntry> entries;
    
    /**
     * Create a new patch, with the given Patch entries. This requires
     * generating the entries in order, in a given syntax, so it is
     * best achieved through the use of the Patch builder.
     * 
     * @param   entries 
     */
    public Patch(List<?> entries) {
    	this.entries = new LinkedList<PatchEntry>();
    	for(Object o : entries) {
    		if(o instanceof PatchEntry) {
    			this.entries.add((PatchEntry)o);
    		}
    	}
    }
    
    /**
     * Create a new, empty patch. This is the degenerate case, applying a
     * patch containing nothing is unspecified but in this implementation
     * will simply do nothing.
     */
    public Patch() {
        this.entries = new LinkedList<PatchEntry>();
    }
    
    /**
     * Apply this patch to a string, returning another string which is the
     * string with the patch applied. If the patch failed to apply, the
     * string will be unchanged.
     * 
     * @param   json    the string to evaluate
     * @return 
     */
    public String apply(String json) {
        try {
            return Json.Map.writeValueAsString(apply(Json.Map.readTree(json)));
        } catch (Exception e) {}
        return json;
    }
    
    /**
     * Apply this patch to a JsonNode, returning the same node which is the
     * node with the patch applied (and the existing node is not preserved). If
     * the patch failed to apply, the node will be unchanged.
     * 
     * @param   node    the node to evaluate
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
     * @param   <T>     type of java object
     * @param   o       java object
     * @return  the modified object
     */
    public <T> Object apply(T o) {
        try {
            JsonNode n = Json.Map.valueToTree(o);
            this.apply(n);
            return Json.Map.treeToValue(n, o.getClass());
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
     * @param   <T>     type of java object
     * @param   json    the patch, as a string
     * @param   o       the object to modify
     * @return  the modified object
     */
    public static <T> Object patch(String json, T o) {
        try {
        	Object val = Json.Map.readValue(json, new TypeReference<List<Patch.PatchEntry>>() { });
        	if(val instanceof List<?>){ 
        		Patch p = new Patch((List<?>) val);
                return p.apply(o);
        	}
        } catch (IOException ex) {
            Logger.getLogger(Patch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (T) o;
    }    
    
    /**
     * The patch is written as a string by writing it out by the global
     * object mapper. The entries are raw JSON of the format defined
     * for JSON patch.
     * 
     * @return 
     */
    @Override
    public String toString() {
        try {
            return Json.Map.writeValueAsString(this.entries);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace(System.out);
            return "[]";
        }
    }
    
    /**
     * Add the contents of a collection of JSON patches together. Note
     * that this method will take the collection of patches provided
     * to it and add the contents of each in order. This ordering is
     * defined on the collection itself, so any complex patches should
     * be given a list which has a definite order.
     * 
     * @param   patches
     * @return  A single patch which contains all patches in order
     */
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
    
    /**
     * Convert a list of path steps like Jackson uses into a JSON Pointer
     * string.
     * 
     * @param objs  A path. Example: fromPath(0, 1, "Testing")
     * @return JSON pointer string
     */
    public static String pointer(Object... objs) {
        StringBuilder pointer = new StringBuilder("");
        for(Object o : objs) {
            pointer.append("/").append(o.toString().replace("~", "~0").replace("/", "~1"));
        }
        return pointer.toString();
    }
    
}
