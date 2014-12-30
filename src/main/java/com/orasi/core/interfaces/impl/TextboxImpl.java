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
@SuppressWarnings("unused")
public class TextboxImpl extends ElementImpl implements Textbox {
    private WebElement element;
//  private java.util.Date date= new java.util.Date();
	private java.util.Date dateAfter= new java.util.Date();
	/**
     * Creates a Element for a given WebElement.
     * @param element element to wrap up
     */
    public TextboxImpl(WebElement element) {
    	super(element);
    }

    /**
     * @summary - Gets the value of an input field. 
     * 		Overrides default clear().
     * @see org.openqa.selenium.WebElement.clear()
     */
    @Override
    public void clear() {
    	TestReporter.debugLog(" Clear text from Textbox [<b>@FindBy: " + getElementLocatorInfo()  + " </b>]");
    	getWrappedElement().clear();
    }

    /**
     * @summary - If the text parameter is not an empty string, this method clears any existing values and 
     * 		performs a "sendKeys(text)" to simulate typing the value. If the text parameter is an empty 
     * 		string, this step is skipped.
     * @param text - text to enter into the field
     */
    @Override
    public void set(String text) {
        if (!text.isEmpty()){          
        	TestReporter.log(" Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        	getWrappedElement().clear();
        	getWrappedElement().sendKeys(text);        	
        }else{
        	dateAfter= new java.util.Date();
        	TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        }
    }
    
    /**
     * @summary - Overloads overridden set() method. If the text parameter is not an empty string, 
     * 		this method uses a JavascriptExecutor to scroll the text field into view and click the 
     * 		text field, then uses Selenium to clear any existing values and performs a "sendKeys(text)" 
     * 		to simulate typing the value. If the text parameter is an empty string, this step is skipped.
     * @param driver - Current active WebDriver object
     * @param text - text to enter into the field
     */
    public void set(WebDriver driver, String text) {
        if (!text.isEmpty()){
        	TestReporter.debugLog(" Send Keys [ <b>" + text.toString() + "</b> ] to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
            JavascriptExecutor executor = (JavascriptExecutor)driver; 
            executor.executeScript("arguments[0].scrollIntoView(true);arguments[0].click();", element);
        	getWrappedElement().clear();
        	getWrappedElement().sendKeys(text); 
        }else{
        	dateAfter= new java.util.Date();
        	TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        }
    }

    /**
     * @summary - If the text parameter is not an empty string, the text field is clicked, 
     * 		then sendKeys() is used to select any/all existing text, type the text passed 
     * 		in the parameter and send a "TAB" key. This is useful if moving from an element 
     * 		is required to trigger underlying JavaScript. If the text parameter is an empty 
     * 		string, this step is skipped. 
     * @param text - text to enter into the field
     */
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

    /**
     * @summary - If the text parameter is not an empty string, the parameter is decoded using 
     * 		the Base64Coder class and a sendKeys() is used to simulate typing the text. If the 
     * 		text parameter is an empty string, this step is skipped. 
     * @param text - text to enter into the field
     */
    public void setSecure(String text) {
        if (!text.isEmpty()){
        	TestReporter.log(" Send encoded text [ <b>" + text.toString() + "</b> ] to Textbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");      	        	
        	getWrappedElement().sendKeys(Base64Coder.decodeString(text).toString());
        }else{
        	dateAfter= new java.util.Date();
        	TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	}
    }
    
    /**
     * @summary - If the text parameter is not an empty string, the text field is clicked, 
     * 		then sendKeys() is used to select any/all existing text, type the decoded 
     * 		parameter (decode the parameter using the Base64Coder) in the text field and 
     * 		send a "TAB" key. This is useful if moving from an element is required to 
     * 		trigger underlying JavaScript. If the text parameter is an empty string, this 
     * 		step is skipped. 
     * @param text - text to enter into the field
     */
    public void safeSetSecure(String text) {
        if (!text.isEmpty()){
        	TestReporter.log(" Send encoded text [ <b>" + text.toString() + "</b> ] to Textbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
        	getWrappedElement().click();        	
        	getWrappedElement().sendKeys(Keys.CONTROL + "a");
        	getWrappedElement().sendKeys(Base64Coder.decodeString(text).toString());
        	getWrappedElement().sendKeys(Keys.TAB);
        }else{
        	dateAfter= new java.util.Date();
        	TestReporter.debugLog(" Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
    	}
    }
    
    /**
     * @summary - If the text parameter is not an empty string, this method clears any 
     * 		existing values and performs a "sendKeys(text)" to simulate typing the 
     * 		value and loops until the text can be verified to exists within the text 
     * 		field. An error is thrown if the text is not found within the timeout 
     * 		window. If the text parameter is an empty string, this step is skipped.
     * @param driver - Current active WebDriver object
     * @param text - text to enter into the field
     */
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

    /**
     * @summary - If the text parameter is not an empty string, the text field is clicked, 
     * 		then sendKeys() is used to select any/all existing text, type the text passed 
     * 		in the parameter and send a "TAB" key. This is useful if moving from an element 
     * 		is required to trigger underlying JavaScript. This method then loops until the 
     * 		text can be verified to exists within the text field. An error is thrown if 
     * 		the text is not found within the timeout window. If the text parameter is an 
     * 		empty string, this step is skipped. 
     * @param driver - Current active WebDriver object
     * @param text - text to enter into the field
     */
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
     * @summary - Gets the value of the attribute "value" of an input field.
     * @return String with the value of the field.
     */
    @Override
    public String getText() {
        return getWrappedElement().getAttribute("value");
    }
}