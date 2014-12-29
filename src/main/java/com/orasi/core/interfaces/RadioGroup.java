package com.orasi.core.interfaces;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.orasi.core.interfaces.impl.RadioGroupImpl;
import com.orasi.core.interfaces.impl.internal.ImplementedBy;

/**
 * Interface for a select element.
 */
@ImplementedBy(RadioGroupImpl.class)
public interface RadioGroup extends Element {
	
	/**
	 * Sets the number of radio buttons found in the group
	 */
	public void setNumberOfRadioButtons();
	
	/**
	 * Returns the number of radio buttons found in the group
	 */
	public int getNumberOfRadioButtons();
	
	/**
	 * Allows a radio button to be selected by its index
	 */
	public void selectByIndex(int index);
	
	/**
	 * Returns a List<String> of all options in the radio group
	 */
	public List<String> getAllOptions();
	
	/**
	 * Allows a radio button to be selected by its value/option text
	 */
	public void selectByOption(String option);
	
	/**
	 * Sets the number of values/options found in the radio group
	 */
	public void setNumberOfOptions();
	
	/**
	 * Returns the number of values/options found in the radio group
	 */
	public int getNumberOfOptions();
	
	/**
	 * Sets the value/option of the selected radio button
	 */
	public void setSelectedOption();
	
	/**
	 * Returns the value/option of the selected radio button
	 */
	public String getSelectedOption();
	
	/**
	 * Returns the index of the selected radio button
	 */
	public int getSelectedIndex();
}