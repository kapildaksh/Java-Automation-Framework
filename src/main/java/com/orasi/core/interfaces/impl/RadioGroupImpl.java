package com.orasi.core.interfaces.impl;

import com.orasi.core.interfaces.RadioGroup;
import com.orasi.core.interfaces.Element;

import org.apache.commons.collections.list.FixedSizeList;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;

import javax.naming.directory.NoSuchAttributeException;

/**
 * Wrapper around a WebElement for the Select class in Selenium.
 */
public class RadioGroupImpl extends ElementImpl implements RadioGroup {

    private List<WebElement> radioButtons = null;
    private java.util.Date date = new java.util.Date();
    private int numberOfRadioButtons;
    private int currentIndex;
    private Element radGroup;
    private List<String> stringOptions;
    private int numberOfOptions;
    private String selectedOption;

    /**
     * Wraps a WebElement with radioGroup functionality.
     *
     * @param element
     *            to wrap up
     * @throws NoSuchAttributeException
     */
    public RadioGroupImpl(final WebElement element) {
	super(element);
	radGroup = new ElementImpl(this.element);
	this.radioButtons = element.findElements(By.tagName("input"));
	getNumberOfRadioButtons();
	getAllOptions();
	Assert.assertNotEquals(radioButtons.size(), 0,
		"No radio buttons were found for the element [" + element
			+ "].");
	currentIndex = getCurrentIndex();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.orasi.core.interfaces.RadioGroup#setNumberOfRadioButtons()
     */
    public void setNumberOfRadioButtons() {
	numberOfRadioButtons = radioButtons.size();
    }

    public int getNumberOfRadioButtons() {
	setNumberOfRadioButtons();
	return numberOfRadioButtons;
    }

    public void selectByIndex(int index) {
	currentIndex = index;
	new ElementImpl(radioButtons.get(currentIndex)).click();
	setSelectedOption();
    }

    @SuppressWarnings("unchecked")
    public List<String> getAllOptions() {
	List<WebElement> options = radGroup.findElements(By.xpath("label"));
	stringOptions = (List<String>) FixedSizeList.decorate(Arrays
		.asList(new String[options.size()]));
	int loopCounter = 0;

	for (WebElement option : options) {
	    stringOptions.set(loopCounter, option.getText());
	    loopCounter++;
	}

	return stringOptions;
    }

    public void setNumberOfOptions() {
	getAllOptions();
	numberOfOptions = stringOptions.size();
    }

    public int getNumberOfOptions() {
	setNumberOfOptions();
	return numberOfOptions;
    }

    public void selectByOption(String option) {
	getAllOptions();
	for (int loopCounter = 0; loopCounter < stringOptions.size(); loopCounter++) {
	    if (stringOptions.get(loopCounter).trim()
		    .equalsIgnoreCase(option.trim())) {
		currentIndex = loopCounter;
		new ElementImpl(radioButtons.get(currentIndex)).click();
		getSelectedOption();
		break;
	    }
	}
	setSelectedOption();
    }

    public void setSelectedOption() {
	getAllOptions();
	selectedOption = stringOptions.get(currentIndex).toString();
    }

    public String getSelectedOption() {
	setSelectedOption();
	return this.selectedOption;
    }

    public int getSelectedIndex() {
	return currentIndex;
    }

    private int getCurrentIndex() {
	String[] attributes = { "checked" };
	int loopCounter = 0;
	int attributeLoopCounter = 0;
	int index = -1;
	String checked = null;

	for (loopCounter = 0; loopCounter < numberOfRadioButtons; loopCounter++) {
	    for (attributeLoopCounter = 0; attributeLoopCounter < attributes.length; attributeLoopCounter++) {
		if (!(radioButtons.get(loopCounter).getAttribute(
			attributes[attributeLoopCounter]) == null)) {
		    checked = radioButtons.get(loopCounter).getAttribute(
			    attributes[attributeLoopCounter]);
		    if (checked.equalsIgnoreCase("true")) {
			index = loopCounter;
			break;

		    }
		}
	    }
	    if (checked != null) {
		break;
	    }
	}
	return index;
    }
}