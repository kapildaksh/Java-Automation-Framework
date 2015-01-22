/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import java.util.List;

/**
 * The ExpectedPath is a class which allows you to patch expected responses
 * from REST services, as well as ignore values in both trees. This is designed
 * to make working with "template data" much more streamlined.
 * 
 * NOTE: You cannot patch the real response, you should only patch the expected
 * response to what the value should return. If there is no way of expecting
 * the exact return value, then ignore the node and check programmatically.
 * 
 * @author Brian Becker
 */
public class ExpectedPath {
    
    private final String newPatchPath;
    private final List<Patch> ignores;
    private final List<Patch> patches;

    public ExpectedPath(String path, List<Patch> ignores, List<Patch> patches) {
        this.newPatchPath = path;
        this.ignores = ignores;
        this.patches = patches;
    }
    
    private void test() {
        if(this.newPatchPath == null)
            throw new IllegalArgumentException("Operation not supported on root node.");
    }
    
    /**
     * Travel to the given path, from this node. Uses the Jackson-style path
     * selection, strings (for object) and integers (for arrays).
     * 
     * @param path  Target path, Integers or Strings only.
     * @return 
     */
    public ExpectedPath path(Object... path) {
        return new ExpectedPath((newPatchPath != null ? newPatchPath : "") + JsonPointer.fromPath(path), ignores, patches);
    }
    
    /**
     * Travel to the given path, from this node. Uses the JSON Pointer style
     * path selection.
     * 
     * @param path  Target path, String specifying JSON Pointer
     * @return 
     */
    public ExpectedPath at(String path) {
        return new ExpectedPath((newPatchPath != null ? newPatchPath : "") + path, ignores, patches);
    }        
    
    /**
     * Ignore a given path in both the Expected Response as well as the
     * Real Response.
     * 
     * @return this
     */
    public ExpectedPath ignore() {
        test();
        Patch p = new Patch.Builder().remove(newPatchPath).build();
        this.ignores.add(p);
        return this;
    }

    /**
     * Replace the expected value at this path with a new value. This can
     * be used when you have changed some of the variables in a template
     * and would like to test just the new values, as well as use the old
     * data for old values.
     * 
     * @param value New expected value
     * @return this
     */
    public ExpectedPath replace(Object value) {
        test();
        Patch p = new Patch.Builder().replace(newPatchPath, value).build();
        this.patches.add(p);
        return this;
    }

    /**
     * Add a value at this path, such as if you are testing the return of
     * a list and have some sort of baseline.
     * 
     * @param value Value to add at this path
     * @return 
     */
    public ExpectedPath add(Object value) {
        test();
        Patch p = new Patch.Builder().add(newPatchPath, value).build();
        this.patches.add(p);
        return this;
    }

    /**
     * Remove this value altogether.
     * 
     * @return 
     */
    public ExpectedPath remove() {
        test();
        Patch p = new Patch.Builder().remove(newPatchPath).build();
        this.patches.add(p);
        return this;
    }
    
}
