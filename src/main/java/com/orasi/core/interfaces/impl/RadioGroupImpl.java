package com.orasi.core.interfaces.impl;

import com.orasi.core.interfaces.RadioGroup;
import com.orasi.utils.TestReporter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;

import java.sql.Timestamp;
import java.util.List;

/**
 * Wrapper around a WebElement for the Select class in Selenium.
 */
public class RadioGroupImpl extends ElementImpl implements RadioGroup {
	public List<WebElement> radioButtons = null;
    private java.util.Date date= new java.util.Date();
    private int numberOfRadioButtons;
    private int currentIndex;
    
    public RadioGroupImpl(WebElement element, WebDriver driver) {
        super(element);
        this.radioButtons = element.findElements(By.tagName("input"));
        Assert.assertNotEquals(radioButtons.size(), 0, "No radio buttons were found for the element ["+element+"].");
        this.currentIndex = -1;
    }
    
    public void setNumberOfRadioButtons(){
    	numberOfRadioButtons = radioButtons.size();
    }
    
    public int getNumberOfRadioButtons(){
    	setNumberOfRadioButtons();
    	return numberOfRadioButtons;
    }
    
    public void selectByIndex(int index){
    	this.currentIndex = index;
    	new ElementImpl(radioButtons.get(currentIndex)).click();
    } 
}