package com.orasi.core.interfaces.impl;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.orasi.core.interfaces.Listbox;
import com.orasi.utils.TestReporter;

/**
 * Wrapper around a WebElement for the Select class in Selenium.
 */
public class ListboxImpl extends ElementImpl implements Listbox {
    private final org.openqa.selenium.support.ui.Select innerSelect;
//    private java.util.Date date= new java.util.Date();
    
    /**
     * @summary - Wraps a WebElement with listbox functionality.
     * @param element - element to wrap up
     */
    public ListboxImpl(WebElement element) {
        super(element);        
        this.innerSelect = new org.openqa.selenium.support.ui.Select(element);
    }
  
    /**
     * @summary - Wraps Selenium's method.
     * @param text - visible text to select
     * @see org.openqa.selenium.support.ui.Select#selectByVisibleText(String)
     */
    @Override
    public void select(String text) {
		if (!text.isEmpty()){
			try{   
			    try{
				innerSelect.selectByVisibleText(text);
			    }catch(RuntimeException rte){
			        TestReporter.interfaceLog("Select option [ <b>" + text.toString() + "</b> ] from Listbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b>]", true);
			        throw rte;
			    }	
			    
			    TestReporter.interfaceLog("Select option [ <b>" + text.toString() + "</b> ] from Listbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b>]");
			}catch (NoSuchElementException e){
				String optionList = "";
				List<WebElement> optionsList= innerSelect.getOptions();
				for(WebElement option : optionsList){
					optionList += option.getText() + " | ";
				}
				TestReporter.interfaceLog(" The value of <b>[ " + text + "</b> ] was not found in Listbox [  <b>@FindBy: " + getElementLocatorInfo()  + " </b>]. Acceptable values are " + optionList +" ]");
				throw new NoSuchElementException("The value of [ " + text + " ] was not found in Listbox [  @FindBy: " + getElementLocatorInfo()  + " ]. Acceptable values are " + optionList );
			}       	
		}else{
		    TestReporter.interfaceLog("Skipping input to Textbox [ <b>@FindBy: " + getElementLocatorInfo()  + " </b> ]");
		}
    }

    /**
     * @summary - Wraps Selenium's method.
     * @see org.openqa.selenium.support.ui.Select#deselectAll()
     */
    @Override
    public void deselectAll() { 
        innerSelect.deselectAll();
    }

    /**
     * @summary - Wraps Selenium's method.
     * @return list of all options in the select.
     * @see org.openqa.selenium.support.ui.Select#getOptions()
     */
    @Override
    public List<WebElement> getOptions() {
        return innerSelect.getOptions();
    }

    /**
     * @summary - Wraps Selenium's method.
     * @param text text to deselect by visible text
     * @see org.openqa.selenium.support.ui.Select#deselectByVisibleText(String)
     */
    @Override
    public void deselectByVisibleText(String text) {
        innerSelect.deselectByVisibleText(text);
    }


    /**
     * @summary - Wraps Selenium's method.
     * @return WebElement of the first selected option.
     * @see org.openqa.selenium.support.ui.Select#getFirstSelectedOption()
     */
    @Override
    public WebElement getFirstSelectedOption() {
        return innerSelect.getFirstSelectedOption();
    }
    
    /**
     * @see org.openqa.selenium.WebElement#isSelected()
     */
    @Override
    public boolean isSelected() {
        return ((WebElement) innerSelect).isSelected();
    }

}