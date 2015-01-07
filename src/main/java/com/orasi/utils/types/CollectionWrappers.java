package com.orasi.utils.types;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * The CollectionWrappers class contains some static methods for working with
 * collection types, such as extracting the values out of a Map or Collection.
 * 
 * @version     01/06/2015
 * @author      Brian Becker
 */
public class CollectionWrappers {
    
    /**
     * This method takes in an object. It takes the object, and if it is a
     * map it returns the value set. If it is a collection, it returns the
     * collection itself. If it is neither, it returns an empty list.
     * 
     * @param       object      Object to extract values, this, or empty collection
     * @return      A collection, empty if the object was invalid
     */
    public static Collection values(Object object) {
        if(object instanceof Map) {
            Map m = (Map) object;
            return m.values();
        } else if(object instanceof Collection) {
            Collection c = (Collection) object;
            return c;
        }
        return Collections.EMPTY_LIST;
    }
    
}
