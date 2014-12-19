package com.orasi.apps.sandbox;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.orasi.utils.PageLoaded;
import com.orasi.core.interfaces.*;
import com.orasi.core.interfaces.impl.internal.ElementFactory;

public class RadioGroupsTest {
	// *****************
	// ** Page Fields **
	// *****************

	private static WebDriver driver;

	// *******************
	// ** Page Elements **
	// *******************

	@FindBy(css = "//input[@type='radio']")
	private RadioGroup rad_1;
	
	@FindBy(id = "page-header")
	private Element elePageHeader;
	
	@FindBy(xpath = "//div[@class='fix-article-body-width nine columns']")
	private RadioGroup div;
	

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
		
		
	}
}
