package com.orasi.utils.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The root node of an ExpectedResponse, this abstract class is extended by
 * implementations of ExpectedResponse to allow traversing the JSON nodes
 * and altering the values.
 * 
 * @author Brian Becker
 */
public class BaseExpectedNode {
    
    protected final String newPatchPath;
    protected final Patch ignores;
    protected final Patch patches;    
    
    public BaseExpectedNode() {
        this("", new Patch(), new Patch());
    }
    
    public BaseExpectedNode(String newPatchPath, Patch ignores, Patch patches) {
        this.newPatchPath = newPatchPath;
        this.ignores = ignores;
        this.patches = patches;
    }
    
    /**
     * Travel to the given path, from this node. Uses the Jackson-style path
     * selection, strings (for object) and integers (for arrays).
     * 
     * @param path  Target path, Integers or Strings only.
     * @return 
     */
    public ExpectedNode path(Object... path) {
        return new ExpectedNode((newPatchPath != null ? newPatchPath : "") + Patch.pointer(path), this);
    }
    
    /**
     * Travel to the given path, from this node. Uses the JSON Pointer style
     * path selection.
     * 
     * @param path  Target path, String specifying JSON Pointer
     * @return 
     */
    public ExpectedNode at(String path) {
        return new ExpectedNode((newPatchPath != null ? newPatchPath : "") + path, this);
    }            
    
    /**
     * Apply the expectation modifications to the real JsonNode.
     * 
     * @param node
     * @param expected
     * @return 
     */
    public JsonNode verify(JsonNode node, JsonNode expected) {
        if(!this.ignores.apply(node).equals(this.ignores.apply(this.patches.apply(expected)))) {
            throw new AssertionError("Node does not match expected, with patches and ignores. \n"+this.ignores.apply(node)+"\n"+this.ignores.apply(this.patches.apply(expected)));
        } else {
            return this.ignores.apply(node);
        }
    }
    
    /**
     * Apply the expectation modifications to the real data. If the data
     * is not JSON data, then we simply compare it with the real string
     * value.
     * 
     * @param node
     * @param expected
     * @return 
     */
    public JsonNode verify(String node, String expected) {
        try {                
            return this.verify(Json.Map.readTree(node), Json.Map.readTree(expected));
        } catch (JsonParseException jpe) {
            if(node.equals(expected)) {
                return new TextNode(node);
            }                
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return MissingNode.getInstance();
    }
    
}
