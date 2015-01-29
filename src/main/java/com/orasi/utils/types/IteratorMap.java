package com.orasi.utils.types;

import java.util.Iterator;

/**
 * Iterator mapping function for transforming the internal elements
 * of collections.
 * 
 * @version     1/06/2015
 * @author      Brian Becker
 * @param       <S>     Input type of mapping function
 * @param       <T>     Output type of mapping function
 */
public abstract class IteratorMap <S,T> implements Iterator <T> {
    private final Iterator<S> data;
    
    /**
     * Create a mapping to the iterator we are calling ourselves with.
     * 
     * @param data 
     */
    public IteratorMap(Iterator data) {
        this.data = data;
    }
    
    /**
     * Do we have more data?
     * 
     * @return 
     */
    @Override
    public boolean hasNext() {
        return this.data.hasNext();
    }

    /**
     * Call the abstract apply function, which we have overridden in a
     * subclass.
     * 
     * @return 
     */
    @Override
    public T next() {
        return this.apply(this.data.next());
    }
    
    /**
     * Apply, this is the operation to perform on each item passing
     * through the iterator before it arrives at its destination.
     * 
     * @param value
     * @return 
     */
    public abstract T apply(S value);

    /**
     * Removing an item from the iterator map is not valid, because we
     * want to maintain the count of items.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("IteratorMap implements a one-way mapping."); //To change body of generated methods, choose Tools | Templates.
    }
}
