package com.orasi.core.interfaces.impl;

import com.orasi.core.interfaces.Link;
import com.orasi.utils.TestReporter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
    	TestReporter.debugLog(" Click Link [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	JavascriptExecutor executor = (JavascriptExecutor)driver;
    	executor.executeScript("arguments[0].click();", element);
    }
    
    @Override
    public void click() {
    	TestReporter.debugLog(" Click Link [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	getWrappedElement().click();
    }
}