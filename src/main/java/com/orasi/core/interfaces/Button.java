package com.orasi.core.interfaces;

import org.openqa.selenium.WebDriver;

import com.orasi.core.interfaces.impl.ButtonImpl;
import com.orasi.core.interfaces.impl.internal.ImplementedBy;


/**
 * Interface that wraps a WebElement in Button functionality. 
 */
@ImplementedBy(ButtonImpl.class)
public interface Button extends Element {
    /**
     * @summary - Click the button using the default Selenium click
     */
	public void click();
	
    /**
     * @summary - Click the button using a JavascriptExecutor click
     * @param driver - Current active WebDriver object
     */
	public void jsClick(WebDriver driver);
}