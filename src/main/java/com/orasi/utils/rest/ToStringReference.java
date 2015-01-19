/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 *
 * @author brian.becker
 * @param <T>
 */
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class ToStringReference<T> {
    private T ref;
    
    public void setReference(T ref) {
        this.ref = ref;
    }
    
    @JsonIgnore
    public T getReference() {
        return this.ref;
    }
    
    public ToStringReference(T ref) {
        this.ref = ref;
    }
    
    @Override
    @JsonValue
    public String toString() {
        return this.ref.toString();
    }
    
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.ref.hashCode();
    }
}
