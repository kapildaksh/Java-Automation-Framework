package com.orasi.utils.rest;

/**
 * The root node of an ExpectedResponse, this abstract class is extended by
 * implementations of ExpectedResponse to allow traversing the JSON nodes
 * and altering the values.
 * 
 * @author Brian Becker
 */
public abstract class BaseExpectedNode {
    
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
        return new ExpectedNode((newPatchPath != null ? newPatchPath : "") + JsonPointer.fromPath(path), this);
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
    
}
