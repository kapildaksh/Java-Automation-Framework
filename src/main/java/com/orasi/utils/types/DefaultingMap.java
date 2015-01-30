package com.orasi.utils.types;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;

/**
 * The Defaulting Map is a way of cascading several maps, and having each
 * of them be checked for a particular value in a certain order. It is not
 * a generic class, generally suited to working with strings. If the value
 * is not found, a string value "{{variablename}}" will be returned rather
 * than a null pointer. This allows for easy search-and-replace operations
 * that will fail visibly.
 * 
 * @author Brian Becker
 */
public class DefaultingMap extends AbstractMap {
    
    public final Map internal;
    
    /**
     * Create a new DefaultingMap.
     * 
     * NOTE: There is no synchronization and this works as a copy, it does
     * not use a reference-based approach.
     * 
     * @param list 
     */
    public DefaultingMap(Map... list) {
        ArrayUtils.reverse(list);   // First is last and last is first...
        internal = new HashMap();
        for(Map m : list) {         // Get all the maps
            if(m != null) {
                internal.putAll(m); // Stuff them into this map class
            }
        }
    }

    @Override
    public Set entrySet() {
        return internal.entrySet(); // Just return the map
    }
    
}
