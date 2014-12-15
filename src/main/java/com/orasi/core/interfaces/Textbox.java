package com.orasi.core.interfaces;

import org.openqa.selenium.WebDriver;

import com.orasi.core.interfaces.impl.TextboxImpl;
import com.orasi.core.interfaces.impl.internal.ImplementedBy;


/**
 * Text field functionality.
 */
@ImplementedBy(TextboxImpl.class)
public interface Textbox extends Element {
	/*
	 * 	 * @see org.openqa.selenium.WebElement#clear()
	 */
	public void clear(); 
	
    /**
     * @param text The text to type into the field.
     */
    void set(String text);
    void set(WebDriver driver, String text);
    
    /**
     * @param text The text to type into the field.
     */
	void safeSet(String text);	
	
	 /**
     * @param text Encoded text to decode then type in the field
     */
	void setSecure(String text);
    
    /**
     * @param text The text to type into the field.
     */
	void safeSetSecure(String text);
	
    /**
     * @param text The text to type into the field.
     */
	void safeSetValidate(WebDriver driver, String text);	
	
    /* 
     * @see org.openqa.selenium.WebElement#getText()
     */
    public String getText();
    
    void setValidate(WebDriver driver, String text);

}