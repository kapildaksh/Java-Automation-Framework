package com.orasi.apps.sandbox;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.utils.PageLoaded;
import com.orasi.core.interfaces.*;
import com.orasi.core.interfaces.impl.RadioGroupImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;

public class RadioGroupsTest {
	// *****************
	// ** Page Fields **
	// *****************

	private static WebDriver driver;

	// *******************
	// ** Page Elements **
	// *******************

	@FindBy(xpath = "/html/body/div/div/div/article/div/ol[2]/li[3]/div/form")
	private RadioGroup rad_1;
	
	@FindBy(id = "page-header")
	private Element elePageHeader;	

	// *********************
	// ** Build page area **
	// *********************
	public RadioGroupsTest(WebDriver driver) {
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	public boolean pageLoaded() {
		return new PageLoaded().isElementLoaded(this.getClass(), driver, elePageHeader);
	}

	public boolean pageLoaded(Element element) {
		return new PageLoaded().isElementLoaded(this.getClass(), driver,
				element);
	}

	public RadioGroupsTest initialize() {
		return ElementFactory.initElements(driver, this.getClass());
	}

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	public void testInteractions() {
		initialize();
		pageLoaded();
		
		rad_1.scrollIntoView(driver);
		
		int numberOfRadioButtons = rad_1.getNumberOfRadioButtons();
		
		
		for(int i = 0; i < numberOfRadioButtons; i++){
			rad_1.selectByIndex(i);
			System.out.println(rad_1.getSelectedIndex());
			System.out.println(rad_1.getSelectedOption());
		}
		
		//rad_1.getAllOptions();
		rad_1.selectByOption("one");
		
		System.out.println(rad_1.getSelectedOption());
		System.out.println(rad_1.getSelectedIndex());
	}
}
