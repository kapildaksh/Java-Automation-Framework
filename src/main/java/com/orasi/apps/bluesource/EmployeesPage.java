package com.orasi.apps.bluesource;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.orasi.core.by.angular.FindByNG;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;

public class EmployeesPage {
	private TestEnvironment te;
	
	//All the page elements
	@FindByNG(ngButtonText= "Add")	
	private Button btnAdd;
	
	@FindBy(css = "input[id = 'search-bar']")
	private Textbox txtSearch;
	
	@FindBy(css = "a.ng-binding")
	private Element tableCell;
	
	@FindBy(css = ".alert-success.alert-dismissable")
	private Label lblSuccessMsg;
	
	@FindBy(className = "table")
	private Webtable tabEmployeeTable;
	
	@FindByNG(ngShow = "showFirstName" )
	private Link lnkFirstNameSort;

	@FindByNG(ngShow = "showLastName" )
	private Link lnkLastNameSort;

	@FindByNG(ngShow = "showTitle" )
	private Link lnkTitleSort;

	@FindByNG(ngShow = "showSupervisor" )
	private Link lnkSupervisorSort;

	@FindByNG(ngShow = "showProject" )
	private Link lnkProjectSort;

	@FindByNG(ngShow = "showLocation" )
	private Link lnkLocationSort;

	@FindByNG(ngShow = "showVacation" )
	private Link lnkVacationSort;

	@FindByNG(ngShow = "showSick" )
	private Link lnkSickSort;

	@FindByNG(ngShow = "showFloatingHoliday" )
	private Link lnkFloatingHolidaySort;
	
	// *********************
	// ** Build page area **
	// *********************
	public EmployeesPage(TestEnvironment te){
		this.te = te;
		ElementFactory.initElements(te.getDriver(), this);
	}
	
	public boolean pageLoaded(){
	    return te.pageLoaded().isElementLoaded(this.getClass(), btnAdd); 
		 
	}
	
	// *****************************************
	// ***Page Interactions ***
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
	
	public void sortOnFirstName(){
	    lnkFirstNameSort.click();
	}
	
	public void sortOnLastName(){
	    lnkLastNameSort.click();
	}
	
	public void sortOnSupervisor(){
	    lnkSupervisorSort.click();
	}
	
	public void sortOnTitle(){
	    lnkTitleSort.click();
	}
	
	public void sortOnProject(){
	    lnkProjectSort.click();
	}
	
	public void sortOnLocation(){
	    lnkLocationSort.click();
	}
	
	public void sortOnVacationDaysLeft(){
	    lnkVacationSort.click();
	}
	
	public void sortOnSickDaysLeft(){
	    lnkSickSort.click();
	}
	
	public void sortFloatingHolidayLeft(){
	    lnkFloatingHolidaySort.click();
	}
	
	public void enterSearchText(String text){
	    txtSearch.syncVisible(te.getDriver());
	    txtSearch.click();
	    txtSearch.safeSet(text);
	    tabEmployeeTable.syncTextInElement(te.getDriver(), text);
	}
	
	
	//search the employee results table by first & last name & click on it
	public boolean searchTableByFirstAndLastName(String firstName, String lastName){
		WebDriverWait wait = new WebDriverWait(te.getDriver(), 30);
		
		//enter the name to search by
		//driver.findElement(By.cssSelector("input[id = 'search-bar']")).sendKeys(firstName + " " + lastName);
		txtSearch.safeSet(firstName + " " + lastName);
		
		//wait for page to load/refresh
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.ng-binding")));
		
		//Get all the elements by CSS in the table that are the individual cells
		List<WebElement> elementList = te.getDriver().findElements(By.cssSelector("a.ng-binding"));
		for (int i = 0; i < elementList.size(); i++){
			
			//if it's the last element then just stop as it won't match
			if (i == elementList.size() - 1){
				
				break;
			}
			if (elementList.get(i).getText().trim().equals(firstName)){
				TestReporter.log("First name was found in table of employees");
				
				//If it matches, then see if the following element matches the last name
				if (elementList.get(i+1).getText().trim().equals(lastName)){
				    TestReporter.log("Last name was found in table of employees");
					//click on the element 
					elementList.get(i).click();
					
					//return true
					TestReporter.log("The employee was found in the table of employees");
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
		List<WebElement> elementList = te.getDriver().findElements(By.cssSelector("a.ng-binding"));
		
		//click on the first one
		elementList.get(0).click();
	}

}
