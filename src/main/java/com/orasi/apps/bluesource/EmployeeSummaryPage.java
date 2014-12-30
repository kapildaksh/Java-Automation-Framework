package com.orasi.apps.bluesource;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;

public class EmployeeSummaryPage {
	private WebDriver driver;
	
	//All the page elements
	@FindBy(xpath = "//*[@id='accordion']/div/div[6]/div[1]/a[2]")
	private Link lnkManageTimeOff;
	
	@FindBy(linkText = "View")
	private Link lnkViewTimeOff;
	
	// *********************
	// ** Build page area **
	// *********************
	public EmployeeSummaryPage(WebDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
		return new PageLoaded().isElementLoaded(this.getClass(), driver, lnkViewTimeOff); 
		  
	}
	
	public EmployeeSummaryPage initialize() {
		return ElementFactory.initElements(driver,
				this.getClass());       
	 }

	// *****************************************
	// ***Page Interactions ***
	// *****************************************
	

	public void ClickManageTimeOff(){
		lnkManageTimeOff.click();
	}
	
	public void ViewManageTimeOff(){
		lnkViewTimeOff.click();
	}
	
	
}
