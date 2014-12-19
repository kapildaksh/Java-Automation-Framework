package com.orasi.core.interfaces;

import com.orasi.core.interfaces.impl.RadioGroupImpl;
import com.orasi.core.interfaces.impl.internal.ImplementedBy;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Interface for a select element.
 */
@ImplementedBy(RadioGroupImpl.class)
public interface RadioGroup extends Element {
	public void setNumberOfRadioButtons();
	
	public int getNumberOfRadioButtons();
	
	public void selectByIndex(int index);
}