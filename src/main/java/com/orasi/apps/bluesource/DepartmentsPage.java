package com.orasi.apps.bluesource;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;
public class DepartmentsPage {
	
	static WebDriver driver;
	
	//All the page elements
	@FindBy(linkText = "Add Department")
	private Link lnkAddDept;
	
	@FindBy(xpath = "//h1[text() = 'Departments']")
	private Label lblTitle;

	@FindBy(css = ".alert-success.alert-dismissable")
	private Label lblSuccessMsg;
	
	//Constructor
	public DepartmentsPage(WebDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
		return new PageLoaded().isElementLoaded(this.getClass(), driver, lnkAddDept); 
		  
	}
	
	public DepartmentsPage initialize() {
		return ElementFactory.initElements(driver,
				this.getClass());       
	 }
	

	//Methods
	
	//click add dept link
	public void clickAddDeptLink(){
		lnkAddDept.click();
	}
	
	public boolean isTitleHeaderDisplayed(){
		return lblTitle.isDisplayed();
	}
	
	//return if the success message is displayed
	public boolean isSuccessMsgDisplayed(){
		return lblSuccessMsg.isDisplayed();
	}
	
	//search page for a dept, return if displayed
	public boolean searchTableByDept(String dept){
		//Get all the rows in the table by CSS
		List<WebElement> elementList = driver.findElements(By.cssSelector(".list-group-item"));
		for(WebElement element:elementList){
			//if it matches the title, then return true
			if(element.getText().contains(dept)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean deleteDept(String dept){
		//Get all the rows in the table by CSS
		List<WebElement> elementList = driver.findElements(By.cssSelector(".list-group-item"));
		for(WebElement element:elementList){
			
			//if it matches the title, then click on the trash element
			if(element.getText().equals(dept)){
		
				//click on the trash element
				element.findElement(By.cssSelector("a[data-method = 'delete']")).click();
				
				//accept the alert that pops up
				Alert alert = driver.switchTo().alert();
				alert.accept();
				return true;
			}
		}
		return false;
	}
	
	

}
