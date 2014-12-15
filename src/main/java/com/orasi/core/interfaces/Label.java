package com.orasi.core.interfaces;

import com.orasi.core.interfaces.impl.LabelImpl;
import com.orasi.core.interfaces.impl.internal.ImplementedBy;


/**
 * Html form label. 
 */
@ImplementedBy(LabelImpl.class)
public interface Label extends Element {
    /**
     * Gets the for attribute on the label.
     *
     * @return string containing value of the for attribute, null if empty.
     */
    String getFor();
}