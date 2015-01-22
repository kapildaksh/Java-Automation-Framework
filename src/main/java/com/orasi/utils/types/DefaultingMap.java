/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.types;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public class DefaultingMap implements Map {

    public final Map orig;
    public final Map fallback;
    
    public final String OPEN;
    public final String CLOSE;
    
    public DefaultingMap(Map orig, Map fallback, String open, String close) {
        this.orig = orig;
        this.fallback = fallback;
        this.OPEN = open;
        this.CLOSE = close;
    }
    
    public DefaultingMap(Map orig, Map fallback) {
        this(orig, fallback, "{{", "}}");
    }
    
    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return true;
    }

    @Override
    public boolean containsValue(Object value) {
        return this.orig.containsValue(value) || this.fallback.containsValue(value) || ( value.toString().startsWith(OPEN) && value.toString().endsWith(CLOSE) );
    }

    @Override
    public Object get(Object key) {
        if(this.orig != null && this.orig.containsKey(key)) {
            return this.orig.get(key);
        } else if(this.fallback != null && this.fallback.containsKey(key)) {
            System.out.println(this.fallback.get(key).toString());
            return this.fallback.get(key);
        } else {
            return (String) OPEN + key.toString() + CLOSE;
        }
    }

    @Override
    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set keySet() {
        Set v = new HashSet();
        if(this.orig != null)
            v.addAll(this.orig.keySet());
        if(this.fallback != null)
            v.addAll(this.fallback.keySet());
        return v;
    }

    @Override
    public Collection values() {
        Set v = new HashSet();
        if(this.orig != null)
            v.addAll(this.orig.values());
        if(this.fallback != null)
            v.addAll(this.fallback.values());
        return v;        
    }

    @Override
    public Set<Entry> entrySet() {
        Set v = new HashSet();
        if(this.orig != null)
            v.addAll(this.orig.entrySet());
        if(this.fallback != null)
            v.addAll(this.fallback.entrySet());
        return v;
    }
    
}
