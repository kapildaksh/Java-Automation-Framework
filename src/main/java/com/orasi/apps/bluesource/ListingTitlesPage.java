package com.orasi.apps.bluesource;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.TestEnvironment;

public class ListingTitlesPage {
    	private TestEnvironment te = null;
	
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
	public ListingTitlesPage(TestEnvironment te){
	    this.te = te;
	    ElementFactory.initElements(te.getDriver(), this);
	}	
	
	public boolean pageLoaded(){
	    return te.pageLoaded().isElementLoaded(this.getClass(), lnkNewTitle);
	}

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	@Step("And I click the \"New Title\" link")
	@Severity(SeverityLevel.CRITICAL)
	public void clickNewTitle(){
		lnkNewTitle.click();
	}
	
	
	public boolean isTitleHeaderDisplayed(){
		return lblTitle.isDisplayed();
	}
	
	@Step("Then an alert should appear for conformation")
	@Severity(SeverityLevel.TRIVIAL)
	public boolean isSuccessMsgDisplayed() {
		WebDriverWait wait = new WebDriverWait(te.getDriver(), 5);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert-success.alert-dismissable")));
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-success.alert-dismissable")));
		return lblSuccessMsg.isDisplayed();
	}
	
	@Step("And the title \"{0}\" should be found on the Titles table")
	@Severity(SeverityLevel.CRITICAL)
	public boolean searchTableByTitle(String title){
		
		//Get all the rows in the table by CSS
		List<WebElement> elementList = te.getDriver().findElements(By.cssSelector("td"));
		for(WebElement element:elementList){
			//if it matches the title, then return true
			if(element.getText().equals(title)){
				return true;
			}
		}
		
		return false;
	}
	
	@Step("And I can delete the title from the table")
	@Severity(SeverityLevel.NORMAL)
	public boolean deleteTitle(String title){
		//Get all the rows in the table by CSS
		List<WebElement> elementList = te.getDriver().findElements(By.cssSelector("td"));
		for(WebElement element:elementList){
			
			//if it matches the title, then click on the trash element
			if(element.getText().equals(title)){
		
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
