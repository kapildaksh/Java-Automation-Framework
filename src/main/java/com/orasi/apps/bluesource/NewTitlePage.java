package com.orasi.apps.bluesource;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;

public class NewTitlePage {
	
	private WebDriver driver;

	//All the page elements
	@FindBy(id = "title_name")
	private Textbox txtTitle;
	
	@FindBy(name = "commit")
	private Button btnCreateTitle;
	
	// *********************
	// ** Build page area **
	// *********************
	public NewTitlePage(WebDriver driver) {
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	public boolean pageLoaded() {
		return new PageLoaded().isElementLoaded(this.getClass(), driver, txtTitle);
	}

	public NewTitlePage initialize() {
		return ElementFactory.initElements(driver, this.getClass());
	}

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	//method to create a new title
	public void createNewTitle(String newTitle){
		if(System.getProperty("operatingSystem").equalsIgnoreCase("mac")){
			txtTitle.set(newTitle);	
		}else{
			txtTitle.safeSet(newTitle);
		}
		btnCreateTitle.click();
	}

}
