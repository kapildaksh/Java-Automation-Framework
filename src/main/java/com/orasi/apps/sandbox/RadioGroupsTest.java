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
		
		RadioGroup rads = new RadioGroupImpl(driver.findElement(By.xpath("/html/body/div/div/div/article/div/ol[2]/li[3]/div/form")), driver);

		rads.selectByIndex(1);
	}
}
