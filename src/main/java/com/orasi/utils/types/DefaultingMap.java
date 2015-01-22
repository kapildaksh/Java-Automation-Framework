/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public DefaultingMap(Map... list) {
        ArrayUtils.reverse(list);   // When we build the 
        internal = new HashMap();
        for(Map m : list) {
            if(m != null) {
                internal.putAll(m);
            }
        }
    }

    @Override
    public Set entrySet() {
        return internal.entrySet();
    }
    
}
