package com.orasi.utils.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private T value;
    
    /**
     * Set the value stored in the reference.
     * 
     * @param   value   Value to set the reference to
     */
    public void set(T value) {
        this.value = value;
    }
    
    /**
     * Get the value stored in the reference.
     * 
     * @return  value stored in the reference
     */
    public T get() {
        return this.value;
    }
    
    /**
     * Create a new reference with a given value, which can be
     * replaced.
     * 
     * @param   value 
     */
    public Reference(T value) {
        this.value = value;
    }
    
    /**
     * Is the value stored in the reference null, or is the value of the
     * reference null. There is a difference, and this is safer against
     * null pointer exceptions.
     * 
     * @param   i       Value to check for null
     * @return 
     */
    public static boolean isNull(Reference i) {
        return i == null || i.value == null;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o instanceof Reference) {
            return this.value.equals(((Reference)o).value);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }
    
    /**
     * Safely get the value, even if the reference is null.
     * 
     * @param   <T>
     * @param   i
     * @return  Value that reference points to
     */
    public static <T> T get(Reference<T> i) {
    	if(i == null) {
    		return (T) null;
    	} else {
    		return i.value;
    	}
    }
    
    /**
     * Safely set the value, even if the reference is null.
     * 
     * @param   <T>     Type of values
     * @param   i       Reference
     * @param   value   Value to assign reference
     */
    public static <T> void set(Reference<T> i, T value) {
        if(i != null) {
            i.value = value;
        }
    }    
    
}
