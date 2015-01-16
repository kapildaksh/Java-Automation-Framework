package com.orasi.apps.bluesource;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.ElementImpl;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;

public class ListingTitlesPage {
	
	private WebDriver driver;
	
	//All the page elements
	@FindBy(linkText = "New Title")	
	private Link lnkNewTitle;
	
	@FindBy(xpath = "//h1[text() = 'Listing titles']")
	private Label lblTitle;

	@FindBy(css = ".alert-success.alert-dismissable")
	private Label lblSuccessMsg;
	
	// *********************
	// ** Build page area **
	// *********************
	public ListingTitlesPage(WebDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
		//return new PageLoaded().isElementLoaded(this.getClass(), driver, lnkNewTitle); 
		return new PageLoaded().isDomInteractive(driver);
		  
	}
	
	public ListingTitlesPage initialize() {
		return ElementFactory.initElements(driver,
				this.getClass());       
	 }

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	public void clickNewTitle(){
		lnkNewTitle.click();
	}
	
	public boolean isTitleHeaderDisplayed(){
		return lblTitle.isDisplayed();
	}
	
	public boolean isSuccessMsgDisplayed() {
		WebDriverWait wait = new WebDriverWait(driver, 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-success.alert-dismissable")));
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success.alert-dismissable")));
		return lblSuccessMsg.isDisplayed();
	}
	
	public boolean searchTableByTitle(String title){
		
		//Get all the rows in the table by CSS
		List<WebElement> elementList = driver.findElements(By.cssSelector("td"));
		for(WebElement element:elementList){
			//if it matches the title, then return true
			if(element.getText().equals(title)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean deleteTitle(String title){
		//Get all the rows in the table by CSS
		List<WebElement> elementList = driver.findElements(By.cssSelector("td"));
		for(WebElement element:elementList){
			//if it matches the title, then click on the trash element
			if(element.getText().equals(title)){
				//click on the trash element
//				Element el = new ElementImpl(element.findElement(By.cssSelector("a[data-method = 'delete']")));
//				el.highlight(driver);
//				el.click();
				
//				element.findElement(By.cssSelector("a[data-method = 'delete']")).click();
				element.findElement(By.cssSelector("a[data-method = 'delete']")).sendKeys("/n");
				
				//accept the alert that pops up
				Alert alert = driver.switchTo().alert();
				alert.accept();
				return true;
			}
		}
		return false;
	}

}
