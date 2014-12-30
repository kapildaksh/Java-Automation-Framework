package com.orasi.apps.bluesource;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;

public class TopNavigationBar {
	private WebDriver driver;
	
	//All the page elements:
	@FindBy(linkText = "Logout")
	private Link lnkLogout;
	
	@FindBy(xpath = "//a[text() = 'Admin ']")
	private Link lnkAdminDrop;
	
	@FindBy(css = "a[href = '/admin/departments']")
	private Link lnkDept;
	
	@FindBy(css = "a[href = '/admin/titles']")
	private Link lnkTitle;
	

	// *********************
	// ** Build page area **
	// *********************
	public TopNavigationBar(WebDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
		return new PageLoaded().isElementLoaded(this.getClass(), driver, lnkLogout); 
		  
	}
	
	public TopNavigationBar initialize() {
		return ElementFactory.initElements(driver,
				this.getClass());       
	 }

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	public void clickAdminLink(){
		lnkAdminDrop.click();
	}
	public void clickDepartmentsLink(){
		lnkDept.click();
	}
	public void clickTitlesLink(){
		lnkTitle.click();
	}
	
	//Verify logout link is displayed
	public boolean isLoggedIn(){
		return lnkLogout.isDisplayed();
	}
	
	//Click logout
	public void logout(){
		lnkLogout.click();
	}
}
