package com.orasi.apps.bluesource;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.Constants;



public class LoginPage {
	static WebDriver driver;
	
	//all the page elements
	@FindBy(id = "employee_username")
	private Textbox txtUsername;
	
	@FindBy(id = "employee_password")
	private  Textbox txtPassword;
	
	@FindBy(name = "commit")
	private static  Button btnLogin;
	
	// *********************
	// ** Build page area **
	// *********************
	public LoginPage(WebDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
		return new PageLoaded().isElementLoaded(this.getClass(), driver, btnLogin); 
		  
	}
	
	public LoginPage initialize() {
		return ElementFactory.initElements(driver,
				this.getClass());       
	 }

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	
	public void login(String role) {
	
		
		
		final String username;
		final String password;
		final ResourceBundle userCredentialRepo = ResourceBundle.getBundle(Constants.USER_CREDENTIALS_PATH);

		username = userCredentialRepo.getString("BLUESOURCE_" + role.toUpperCase());
		password = userCredentialRepo.getString("BLUESOURCE_PASSWORD");
		
		 
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.elementToBeClickable(txtUsername));
		txtUsername.safeSet(username);
		txtPassword.safeSet(password);
		btnLogin.click();
	}
	

	  

}
