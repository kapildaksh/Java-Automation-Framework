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
    
    public void set(T value) {
        this.value = value;
    }
    
    public T get() {
        return this.value;
    }
    
    public Reference(T value) {
        this.value = value;
    }
    
    @JsonIgnore
    public boolean isNull() {
        return value == null;
    }
    
    public static boolean isNull(Reference i) {
        return i == null || i.isNull();
    }
    
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
    
    @JsonValue
    public String name () {
        return this.value.toString();
    }
    
    @JsonCreator
    public static Reference<Object> name (String ref) {
        return new Reference<Object>(ref);
    }
    
    public static <T> T safeGet(Reference<T> i) {
        return i == null ? null : (i.value == null) ? null : i.value;
    }
    
}
