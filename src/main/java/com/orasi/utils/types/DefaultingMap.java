/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.types;

import com.google.common.base.Function;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author brian.becker
 * @param <K>
 * @param <V>
 */
public class DefaultingMap<K, V> implements Map<K, V> {

    public final Map orig;
    public final Map fallback;
    
    public final Function<K, V> keyDefaults;
    
    public DefaultingMap(Map<K, V> orig, Map<K, V> fallback, Function<K, V> keyDefaults) {
        this.orig = orig != null ? orig : new HashMap();
        this.fallback = fallback != null ? orig : new HashMap();
        this.keyDefaults = keyDefaults;
    }
    
    @Override
    public int size() {
        if(keyDefaults != null) return Integer.MAX_VALUE;
        return this.keySet().size();
    }

    @Override
    public boolean isEmpty() {
        if(keyDefaults != null) return false;
        return this.orig.isEmpty() && this.fallback.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if(keyDefaults != null) return true;
        return this.orig.containsKey(key) || this.fallback.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.orig.containsValue(value) || this.fallback.containsValue(value);
    }

    @Override
    public V get(Object key) {
        if(this.orig.containsKey(key)) {
            return (V) this.orig.get(key);
        } else if(this.fallback.containsKey(key)) {
            return (V) this.fallback.get(key);
        } else if(keyDefaults != null) {
            return (V) keyDefaults.apply((K) key);
        }
        return null;        
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<K> keySet() {
        Set v = new HashSet();
        v.addAll(this.orig.keySet());
        v.addAll(this.fallback.keySet());
        return v;
    }

    @Override
    public Collection<V> values() {
        Set v = new HashSet();
        v.addAll(this.orig.values());
        v.addAll(this.fallback.values());
        return v;        
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set v = new HashSet();
        v.addAll(this.orig.entrySet());
        v.addAll(this.fallback.entrySet());
        return v;
    }
    
}
