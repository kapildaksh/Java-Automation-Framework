/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author brian.becker
 */
public class ObjectMap implements Map {
    private final Object object;
    private final String prefix;
    
    public ObjectMap(Object o, String methodPrefix) {
        this.object = o;
        this.prefix = methodPrefix;
    }
    
    public ObjectMap(Object o) {
        this(o, "p:");
    }

    public int size() {
        return object.getClass().getFields().length;
    }

    public boolean isEmpty() {
        return object.getClass().getFields().length == 0;
    }
    
    @SuppressWarnings("UseSpecificCatch")
    public Object field(Field fld, Object... value) {
        try {
            fld.setAccessible(true);
            Object o = fld.get(object);
            if(value.length == 1)
                fld.set(object, value[0]);
            fld.setAccessible(false);
            return o;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public boolean isAccessor(String accessor) {
        return 
                (accessor.startsWith("get") || accessor.startsWith("set")) && 
                accessor.length() > 3 && 
                Character.isUpperCase(accessor.charAt(3));
    }
    
    public String removeAccessor(String accessor) {
        StringBuilder b = new StringBuilder("");
        if(isAccessor(accessor)) {
            b.append(prefix);
            b.append(Character.toLowerCase(accessor.charAt(3)));
            b.append(accessor.substring(4));
        }   
        return b.toString();
    }

    @SuppressWarnings("UseSpecificCatch")
    public boolean containsKey(Object key) {
        Method[] methods = object.getClass().getMethods();
        for(Method m : methods) {
            if(isAccessor(m.getName()) && removeAccessor(m.getName()).equals(key.toString())) {
                return true;
            }
        }
        
        try {
            object.getClass().getField(key.toString());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public boolean containsValue(Object value) {
        try {
            Method[] methods = object.getClass().getMethods();
            for(Method m : methods) {
                if(m.getParameterTypes().length == 0 && isAccessor(m.getName())) {
                    if(m.invoke(object, new Object[] {}).equals(value)) {
                        return true;
                    }
                }
            }

            for(Field f : object.getClass().getFields()) {
                if (field(f).equals(value)) {
                    return true;
                }
            }
        } catch (Exception ex) {
        }
        return false;
    }

    @SuppressWarnings("UseSpecificCatch")
    public Object get(Object key) {
        try {
            Method[] methods = object.getClass().getMethods();
            for(Method m : methods) {
                if(m.getParameterTypes().length == 0 && isAccessor(m.getName()) && removeAccessor(m.getName()).equals(key.toString())) {
                    return m.invoke(object, new Object[] {});
                }
            }
            Field fld = object.getClass().getDeclaredField(key.toString());
            return field(fld);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            throw new NullPointerException("Property not available (for retrieval) in object mapping");
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public Object put(Object key, Object value) {
        try {
            Method[] methods = object.getClass().getMethods();
            for(Method m : methods) {
                if(m.getParameterTypes().length == 1 && isAccessor(m.getName()) && removeAccessor(m.getName()).equals(key.toString())) {
                        return m.invoke(object, new Object[] { value });
                }
            }            
            Field fld = object.getClass().getDeclaredField(key.toString());
            return field(fld, value);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            throw new IllegalArgumentException("Property not available (for setting) in object mapping");
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    public Object remove(Object key) {
        throw new UnsupportedOperationException("Property cannot be removed from underlying object");        
    }

    public void putAll(Map m) {
        for (Object oentry : m.entrySet()) {
            Map.Entry entry = (Map.Entry) oentry;
            put(entry.getKey(), entry.getValue());
        }
    }
    
    public void putAllExists(Map m) {
        for (Object oentry : m.entrySet()) {
            Map.Entry entry = (Map.Entry) oentry;
            if(this.containsKey(entry.getKey())) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    public void clear() {
        throw new UnsupportedOperationException("Properties cannot be removed from underlying object");        
    }

    public Set keySet() {
        return this.asHashMap().keySet();
    }

    @SuppressWarnings("UseSpecificCatch")
    public Collection values() {
        return this.asHashMap().values();
    }

    @SuppressWarnings("UseSpecificCatch")
    public Set entrySet() {
        return this.asHashMap().entrySet();
    }
    
    @SuppressWarnings("UseSpecificCatch")
    public Map asHashMap() {
        Map s = new HashMap();
        for(Field f : object.getClass().getDeclaredFields()) {
            try {
                s.put(f.getName(), field(f));
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }
        return s;
    }
    
    @Override
    public String toString() {
        return this.asHashMap().toString();
    }
}
