package com.orasi.apps.bluesource;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;

public class NewDeptPage {

	private WebDriver driver;

	//All the page elements
	@FindBy(id = "department_name")
	private Textbox txtDept;
	
	@FindBy(id = "department_department_id")
	private Textbox txtParentDept;
	
	@FindBy(name = "commit")
	private Button btnCreateDept;
	
	// *********************
	// ** Build page area **
	// *********************
	public NewDeptPage(WebDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
		return new PageLoaded().isElementLoaded(this.getClass(), driver, txtDept); 
		  
	}
	
	public NewDeptPage initialize() {
		return ElementFactory.initElements(driver,
				this.getClass());       
	 }

	// *****************************************
	// ***Page Interactions ***
	// *****************************************
	
	
	//method to create a new title
	public void CreateNewDept(String dept){
		txtDept.safeSet(dept);
		btnCreateDept.click();
	}
}
