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
 * @param <S>
 * @param <T>   Output type of mapping function
 */
public abstract class IteratorMap <S,T> implements Iterator <T> {
    private final Iterator<S> data;
    
    public IteratorMap(Iterator data) {
        this.data = data;
    }
    
    @Override
    public boolean hasNext() {
        return this.data.hasNext();
    }

    @Override
    public T next() {
        return this.apply(this.data.next());
    }
    
    public abstract T apply(S value);

    @Override
    public void remove() {
        throw new UnsupportedOperationException("IteratorMap implements a one-way mapping."); //To change body of generated methods, choose Tools | Templates.
    }
}
