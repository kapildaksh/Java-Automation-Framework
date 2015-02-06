package com.orasi.utils.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;

/**
 * This is a reference by name, which works like a holder but is designed
 * to store an object by name in a central repository.
 * 
 * @author Brian Becker
 * @param <T>
 */
public class Name<T> extends Reference<T> {
    
    /**
     * Create a new reference with a given value, which can be
     * replaced.
     * 
     * @param value 
     */
    public Name(T value) {
        super(value);
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
        if(o instanceof Name) {
            return this.name().equals(((Name)o).name());
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
     * @return  Name of the object
     */
    @JsonValue
    public String name () {
        return this.get().toString();
    }
    
    /**
     * Get a reference with the given name. We add a NoValue into the
     * reference to signify that it has no real value assigned to it.
     * This implementation could use a centralized repository, which
     * would provide the ability to determine if the reference truly
     * is invalid.
     * 
     * NOTE: With no centralized lookup repository, the fact that one has
     * a reference with a name and a null does not mean there is actually
     * no value assigned to another given object.
     * 
     * @param   <T>     Type of the object
     * @param   ref     Name of the object
     * @return	The Name with an associated value 
     */
    @JsonCreator
    public static <T> Name<T> name (String ref) {
        return new Name<T>(null);
    }
    
}
