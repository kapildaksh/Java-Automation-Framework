package com.orasi.core.interfaces.impl.internal;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orasi.core.interfaces.Element;
import com.orasi.utils.Sleeper;
import com.orasi.utils.WebDriverSetup;

public class PageLoaded {
	
	private WebDriver driver = null;
	private Class clazz = null;
	
	private void initialize() {
	    ElementFactory.initElements(driver, clazz);	        
	}
	
	public boolean isPageHTMLLoaded(Class clazz, WebDriver driver, Element obj){
		this.driver = driver;
		this.clazz = clazz;		
		int timeout = 20;
		int count = 0;
		
		WebDriverWait wait = new WebDriverWait(driver, 1);
		try{
			// && wait.until(ExpectedConditions.elementToBeClickable(obj)) != null
			while(!obj.elementWired()){
				if (count == timeout){
					break;
				}else{
					Sleeper.sleep(1000);
					count++;
					initialize();
				}
			}
		}catch( NullPointerException | NoSuchElementException |StaleElementReferenceException e){
			// do nothing
		}
		if (count < timeout){
			return true;
		}else{
			return false;
		}
	}
}
