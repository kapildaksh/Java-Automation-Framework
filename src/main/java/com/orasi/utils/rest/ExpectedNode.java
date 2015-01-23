package com.orasi.utils.rest;

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
public class ExpectedNode extends BaseExpectedNode {
    
    public ExpectedNode(String path, BaseExpectedNode base) {
        super(path, base.ignores, base.patches);
    }
    
    /**
     * Ignore a given path in both the Expected Response as well as the
     * Real Response.
     * 
     * @return this
     */
    public ExpectedNode ignore() {
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
    public ExpectedNode replace(Object value) {
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
    public ExpectedNode add(Object value) {
        Patch p = new Patch.Builder().add(newPatchPath, value).build();
        this.patches.add(p);
        return this;
    }

    /**
     * Remove this value altogether.
     * 
     * @return 
     */
    public ExpectedNode remove() {
        Patch p = new Patch.Builder().remove(newPatchPath).build();
        this.patches.add(p);
        return this;
    }
    
}
