package com.orasi.utils.rest;

/**
 * This is an opaque container for holding a JSON pointer. It also has a few
 * helper methods for converting other notations into JSON pointer.
 * 
 * @author Brian Becker
 */
public class JsonPointer {
    
    private final String pointer;
    
    /**
     * Create a JSON pointer from a JSON pointer string.
     * 
     * @param pointer 
     */
    public JsonPointer(String pointer) {
        this.pointer = pointer;
    }
    
    /**
     * Convert a list of path steps like Jackson uses into a JSON Pointer
     * string.
     * 
     * @param objs  A path. Example: fromPath(0, 1, "Testing")
     * @return JSON pointer string
     */
    public static String fromPath(Object... objs) {
        StringBuilder pointer = new StringBuilder("");
        for(Object o : objs) {
            pointer.append("/").append(o.toString().replace("~", "~0").replace("/", "~1"));
        }
        return pointer.toString();
    }
    
}
