/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
    
    @JsonValue
    @Override
    public String toString() {
        return this.value.toString();
    }
    
}
