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
    
    public static class NoValue { }
    
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
     * issues with JSON serialization.
     * 
     * NOTE: With no centralized lookup repository, the fact that one has
     * a reference with a name and a NoValue does not mean there is actually
     * no value assigned to another given object.
     * 
     * @param   o   object to compare with
     * @return  are the two objects equal
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
     * Get a reference with the given name. We add a NoValue into the
     * reference to signify that it has no real value assigned to it.
     * This implementation could use a centralized repository, which
     * would provide the ability to determine if the reference truly
     * is invalid.
     * 
     * NOTE: With no centralized lookup repository, the fact that one has
     * a reference with a name and a NoValue does not mean there is actually
     * no value assigned to another given object.
     * 
     * @param ref
     * @return 
     */
    @JsonCreator
    public static Reference<Object> name (String ref) {
        return new Reference<Object>(new NoValue());
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
