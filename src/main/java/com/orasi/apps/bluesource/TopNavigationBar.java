package com.orasi.apps.bluesource;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.TestEnvironment;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;

public class TopNavigationBar {
    	private TestEnvironment te = null;
	
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
	public TopNavigationBar(TestEnvironment te){
	    this.te = te;
	    ElementFactory.initElements(te.getDriver(), this);
	}
	
	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	@Step("And I click the \"Admin Link\"")
	public void clickAdminLink(){
		lnkAdminDrop.click();
	}
	
	@Step("And I navigated to the \"Departments Page\"")
	public void clickDepartmentsLink(){
		lnkDept.click();
	}
	
	@Step("And I navigated to the \"Titles Page\"")
	public void clickTitlesLink(){
		lnkTitle.click();
	}
	
	//Verify logout link is displayed
	public boolean isLoggedIn(){
		return lnkLogout.isDisplayed();
	}
	
	//Click logout
	@Step("And I Log Out")
	public void logout(){
		lnkLogout.click();
	}
}
