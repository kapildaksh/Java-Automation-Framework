/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.types;

import java.util.Iterator;

/**
 * Iterator mapping function for transforming the internal elements
 * of collections.
 * 
 * @author brian.becker
 * @param <S>   Input type of mapping function
 * @param <T>   Output type of mapping function
 */
public abstract class IteratorMap <S,T> implements Iterator <T> {
    private final Iterator<S> data;
    
    public IteratorMap(Iterator<S> data) {
        this.data = data;
    }
    
    @Override
    public boolean hasNext() {
        return data.hasNext();
    }

    @Override
    public abstract T next();

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Data providers are read only."); //To change body of generated methods, choose Tools | Templates.
    }
}
