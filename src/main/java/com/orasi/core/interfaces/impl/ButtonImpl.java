package com.orasi.core.interfaces.impl;

import java.sql.Timestamp;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.orasi.core.interfaces.Button;
import com.orasi.utils.TestReporter;
import com.orasi.utils.WebDriverSetup;

/**
 * Wraps a label on a html form with some behavior.
 */
public class ButtonImpl extends ElementImpl implements Button {
	private java.util.Date date= new java.util.Date();
    /**
     * Creates a Element for a given WebElement.
     *
     * @param element element to wrap up
     */
    public ButtonImpl(WebElement element) {
        super(element);
    }
    
    @Override
    public void click() {

    	TestReporter.log("Click Button [ <b>@FindBy: " + getElementLocatorInfo()  + " </b>]");

    	getWrappedElement().click();
    	//JavascriptExecutor jse = (JavascriptExecutor)WebDriverSetup.driver;
    	//jse.executeScript("arguments[0].click();", element );
 
    }
    
    @Override
    public void jsClick(WebDriver driver){

    	TestReporter.log("Click Button [ <b>@FindBy: " + getElementLocatorInfo()  + " </b>]");

    	JavascriptExecutor jse = (JavascriptExecutor)driver;
    	jse.executeScript("arguments[0].click();", element );
    }
    
    public void mouseClick(){
    	Point xy = element.getLocation();
    //	new Mouse(WebDriverSetup.driver).click(xy.getX(), xy.getY());
    }
   

//    @Override
//    public String getFor() {
//        return getWrappedElement().getAttribute("for");
//    }
}