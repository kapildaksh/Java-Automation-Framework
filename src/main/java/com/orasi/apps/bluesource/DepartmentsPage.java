package com.orasi.apps.bluesource;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.TestEnvironment;
public class DepartmentsPage {
	
    	private TestEnvironment te = null;
	
	//All the page elements
	@FindBy(linkText = "Add Department")
	private Link lnkAddDept;
	
	@FindBy(xpath = "//h1[text() = 'Departments']")
	private Label lblTitle;

	@FindBy(css = ".alert-success.alert-dismissable")
	private Label lblSuccessMsg;
	
	// *********************
	// ** Build page area **
	// *********************
	public DepartmentsPage(TestEnvironment te){
		this.te = te;
		ElementFactory.initElements(te.getDriver(), this);
	}
	

	//Methods
	
	//click add dept link
	@Step("And I click the \"New Department\" link")
	public void clickAddDeptLink(){
		lnkAddDept.click();
	}
	
	public boolean isTitleHeaderDisplayed(){
		return lblTitle.isDisplayed();
	}
	
	//return if the success message is displayed
	@Step("Then an alert should appear for conformation")
	public boolean isSuccessMsgDisplayed(){
		return lblSuccessMsg.isDisplayed();
	}
	
	//search page for a dept, return if displayed
	@Step("And the department \"{0}\" should be found on the Titles table")
	public boolean searchTableByDept(String dept){
		//Get all the rows in the table by CSS
		List<WebElement> elementList = te.getDriver().findElements(By.cssSelector(".list-group-item"));
		for(WebElement element:elementList){
			//if it matches the title, then return true
			if(element.getText().contains(dept)){
				return true;
			}
		}
		
		return false;
	}
	
	@Step("And I can delete the department from the table")
	public boolean deleteDept(String dept){
		//Get all the rows in the table by CSS
		List<WebElement> elementList = te.getDriver().findElements(By.cssSelector(".list-group-item"));
		for(WebElement element:elementList){
			
			//if it matches the title, then click on the trash element
			if(element.getText().contains(dept)){
		
				//click on the trash element
				element.findElement(By.cssSelector("a[data-method = 'delete']")).click();
				
				//accept the alert that pops up
				Alert alert = te.getDriver().switchTo().alert();
				alert.accept();
				return true;
			}
		}
		return false;
	}
	
	

}
