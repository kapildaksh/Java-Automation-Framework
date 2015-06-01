package com.orasi.core.interfaces.impl;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.orasi.core.interfaces.Link;
import com.orasi.utils.TestReporter;

/**
 * Wraps a label on a html form with some behavior.
 */
public class LinkImpl extends ElementImpl implements Link {
	//private java.util.Date date= new java.util.Date();
    /**
     * Creates a Element for a given WebElement.
     *
     * @param element element to wrap up
     */
    public LinkImpl(WebElement element) {
        super(element);
    }
    
    @Override
    public void jsClick(WebDriver driver) {
    	JavascriptExecutor executor = (JavascriptExecutor)driver;
    	try{
    	    executor.executeScript("arguments[0].click();", element);
    	}catch(RuntimeException rte){
    	    TestReporter.interfaceLog(" Click Link [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]", true);
    	    throw rte;
    	}
    	TestReporter.interfaceLog(" Click Link [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        
    }
    
    @Override
    public void click() {    	
        try{
            getWrappedElement().click();
        }catch(RuntimeException rte){
            TestReporter.interfaceLog(" Click Link [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]", true);
            throw rte;
        }
    	TestReporter.interfaceLog(" Click Link [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	
    }
}
