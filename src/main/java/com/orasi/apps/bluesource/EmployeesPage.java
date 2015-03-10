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
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;

public class EmployeesPage {
	private WebDriver driver;
	
	//All the page elements
	@FindBy(css = "button[type = 'submit']")	
	private Button btnAdd;
	
	@FindBy(css = "input[id = 'search-bar']")
	private Textbox txtSearch;
	
	@FindBy(css = "a.ng-binding")
	private Element tableCell;
	
	@FindBy(css = ".alert-success.alert-dismissable")
	private Label lblSuccessMsg;
	
	// *********************
	// ** Build page area **
	// *********************
	public EmployeesPage(WebDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
		return new PageLoaded().isElementLoaded(this.getClass(), driver, btnAdd); 
		  
	}
	
	public EmployeesPage initialize() {
		return ElementFactory.initElements(driver,
				this.getClass());       
	 }
	
	// *****************************************
	// ***Page Interactions ***trfd
	// *****************************************
	
	//Click the add button
	public void clickAddNewEmployee(){
		//Click add
		btnAdd.click();
	}
	
	public boolean isSuccessMsgDisplayed(){
		return lblSuccessMsg.isDisplayed();
	}
	
	public String getSuccessMsgText(){
		return lblSuccessMsg.getText();
	}
	
	//search the employee results table by first & last name & click on it
	public boolean searchTableByFirstAndLastName(String firstName, String lastName){
		WebDriverWait wait = new WebDriverWait(driver, 30);
		
		//enter the name to search by
		//driver.findElement(By.cssSelector("input[id = 'search-bar']")).sendKeys(firstName + " " + lastName);
		txtSearch.safeSet(firstName + " " + lastName);
		
		//wait for page to load/refresh
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.ng-binding")));
		
		//Get all the elements by CSS in the table that are the individual cells
		List<WebElement> elementList = driver.findElements(By.cssSelector("a.ng-binding"));
		for (int i = 0; i < elementList.size(); i++){
			
			//if it's the last element then just stop as it won't match
			if (i == elementList.size() - 1){
				
				break;
			}
			if (elementList.get(i).getText().trim().equals(firstName)){
				Reporter.log("First name was found in table of employees");
				
				//If it matches, then see if the following element matches the last name
				if (elementList.get(i+1).getText().trim().equals(lastName)){
					Reporter.log("Last name was found in table of employees");
					//click on the element 
					elementList.get(i).click();
					
					//return true
					Reporter.log("The employee was found in the table of employees");
					return true;
				}
			}
		}
		
		//element not found that matches first and last name
		Reporter.log("The employee was not found in search results" + firstName + " " + lastName );
		return false;
		
	}
	
	//This method just selects the first employee in the employee table - so you don't need to know
	//who the employee is
	public void selectFirstEmployee(){
		
		//Get all the table elements
		List<WebElement> elementList = driver.findElements(By.cssSelector("a.ng-binding"));
		
		//click on the first one
		elementList.get(0).click();
	}

}
