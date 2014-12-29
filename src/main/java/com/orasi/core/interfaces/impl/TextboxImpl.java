package com.orasi.core.interfaces.impl;

import java.sql.Timestamp;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Textbox;
import com.orasi.utils.Base64Coder;
import com.orasi.utils.TestReporter;
import com.orasi.utils.WebDriverSetup;

/**
 * TextInput  wrapper.
 */
public class TextboxImpl extends ElementImpl implements Textbox {
    private WebElement element;
    private java.util.Date date= new java.util.Date();
    private java.util.Date dateAfter= new java.util.Date();
	/**
     * Creates a Element for a given WebElement.
     *
     * @param element element to wrap up
     */
    public TextboxImpl(WebElement element) {
    	super(element);
    }

    @Override
    public void clear() {
    	TestReporter.debugLog(" Clear text from Textbox [<b>@FindBy: " + getElementLocatorInfo()  + " </b>]");
    	getWrappedElement().clear();
    }

    @Override
    public void set(String text) {
    	
        //if (text != ""){
        if (!text.isEmpty()){
        //	JavascriptExecutor executor = (JavascriptExecutor)WebDriverSetup.driver; 
        //	executor.executeScript("arguments[0].scrollIntoView(true);arguments[0].click();", element);
          
        	TestReporter.debugLog(" Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        	getWrappedElement().clear();
        	getWrappedElement().sendKeys(text);        	
        }else{
        	dateAfter= new java.util.Date();
        	TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        }
    }
    
    public void set(WebDriver driver, String text) {
        if (!text.isEmpty()){
        	TestReporter.debugLog(" Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
            JavascriptExecutor executor = (JavascriptExecutor)driver; 
            executor.executeScript("arguments[0].scrollIntoView(true);arguments[0].click();", element);
        }else{
        	dateAfter= new java.util.Date();
        	TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        }
    }

    public void safeSet(String text) {
        if (!text.isEmpty()){
        	TestReporter.debugLog(" Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        	getWrappedElement().click();        	
        	getWrappedElement().sendKeys(Keys.CONTROL + "a");
        	getWrappedElement().sendKeys(text);
        	getWrappedElement().sendKeys(Keys.TAB);
        }else{
        	dateAfter= new java.util.Date();
        	TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	}
    }
    

    public void setSecure(String text) {
        if (!text.isEmpty()){
        	TestReporter.debugLog(" Send encoded text [ <b>" + text.toString() + "</b> ] to Textbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        	//getWrappedElement().click();        	        	
        	getWrappedElement().sendKeys(Base64Coder.decodeString(text).toString());
        }else{
        	dateAfter= new java.util.Date();
        	TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	}
    }
    
    public void safeSetSecure(String text) {
        if (!text.isEmpty()){
        	TestReporter.debugLog(" Send encoded text [ <b>" + text.toString() + "</b> ] to Textbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        	//getWrappedElement().click();
        	getWrappedElement().click();        	
        	getWrappedElement().sendKeys(Keys.CONTROL + "a");
        	getWrappedElement().sendKeys(Base64Coder.decodeString(text).toString());
        	getWrappedElement().sendKeys(Keys.TAB);
        }else{
        	dateAfter= new java.util.Date();
        	TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	}
    }
    
    public void setValidate( WebDriver driver, String text){
    	if(!text.isEmpty()){
    		TestReporter.debugLog(" Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    		Element obj = new ElementImpl(getWrappedElement());
    		obj.syncEnabled(driver);
    		getWrappedElement().clear();
    		getWrappedElement().sendKeys(text);
    		obj.syncTextInElement(driver, text, 3, true);
    		dateAfter= new java.util.Date();
    		TestReporter.debugLog(" VALIDATED [ <b>" + text.toString() + "</b> ] was entered in the textbox."); 
    	}else{
    		dateAfter= new java.util.Date();
    		TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	}
    }
    
    public void safeSetValidate(WebDriver driver, String text){
    	if(!text.isEmpty()){
    		TestReporter.debugLog(" Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    		Element obj = new ElementImpl(getWrappedElement());
    		obj.syncEnabled(driver);
    		getWrappedElement().click();        	
        	getWrappedElement().sendKeys(Keys.CONTROL + "a");
        	getWrappedElement().sendKeys(text);
        	getWrappedElement().sendKeys(Keys.TAB);
    		obj.syncTextInElement(driver, text, 3, true);
    		dateAfter= new java.util.Date();
    		TestReporter.debugLog(" VALIDATED [ <b>" + text.toString() + "</b> ] was entered in the textbox."); 
    	}else{
    		dateAfter= new java.util.Date();
    		TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	}
    }
    /**
     * Gets the value of an input field.
     * @return String with the value of the field.
     */
    @Override
    public String getText() {
        return getWrappedElement().getAttribute("value");
    }
}