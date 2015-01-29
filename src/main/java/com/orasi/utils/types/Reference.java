/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/**
 * This is a reference type which is like a non-synchronous version of the
 * AtomicReference.
 * 
 * @author Brian Becker
 * @param <T>
 */
public class Reference<T> {
    
    @JsonIgnore
    public T value;
    
    /**
     * Set the value stored in the reference.
     * 
     * @param value 
     */
    public void set(T value) {
        this.value = value;
    }
    
    /**
     * Get the value stored in the reference.
     * 
     * @return 
     */
    public T get() {
        return this.value;
    }
    
    /**
     * Create a new reference with a given value, which can be
     * replaced.
     * 
     * @param value 
     */
    public Reference(T value) {
        this.value = value;
    }
    
    /**
     * Is the reference null?
     * 
     * @return 
     */
    @JsonIgnore
    public boolean isNull() {
        return value == null;
    }
    
    /**
     * Is the value stored in the reference null, or is the value of the
     * reference null. There is a difference, and this is safer against
     * null pointer exceptions.
     * 
     * @param i
     * @return 
     */
    public static boolean isNull(Reference i) {
        return i == null || i.isNull();
    }
    
    /**
     * The reference name being equal means the reference is equal. This
     * is an assumption which is made to allow for working around a few
     * 
     * 
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof Reference) {
            return this.name().equals(((Reference)o).name());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name());
    }
    
    /**
     * The JSON serialization is simply the name of the reference. This means
     * all references will only ever return the value of the reference's
     * name, never the value.
     * 
     * @return 
     */
    @JsonValue
    public String name () {
        return this.value.toString();
    }
    
    /**
     * Get a reference with the given name.
     * 
     * @param ref
     * @return 
     */
    @JsonCreator
    public static Reference<Object> name (String ref) {
        return new Reference<Object>(ref);
    }
    
    /**
     * Safely get the value, even if the reference is null.
     * 
     * @param <T>
     * @param i
     * @return 
     */
    public static <T> T safeGet(Reference<T> i) {
        return i == null ? null : (i.value == null) ? null : i.value;
    }
    
}
