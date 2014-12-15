package com.orasi.utils.exception.uimessage;

import org.openqa.selenium.WebDriver;

import com.orasi.utils.exception.automation.WebElementNotFoundException;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class WebUiModalNotFoundException extends WebElementNotFoundException {

    private static final long serialVersionUID = 1L;

    public WebUiModalNotFoundException() {
        super("Modal not found");
    }
    
    public WebUiModalNotFoundException(String message) {
        super("Modal not found: " + message);
    }
    
    public WebUiModalNotFoundException(String message, Throwable cause) {
        super("Modal not found: " + message, cause);
    }
    
    public WebUiModalNotFoundException(WebDriver driver) {
        super("Modal not found: ", driver);
    }
    
    public WebUiModalNotFoundException(String message, WebDriver driver) {
        super("Modal not found: " + message, driver);
    }
    
    public WebUiModalNotFoundException(String message, Throwable cause, WebDriver driver) {
        super("Modal not found: " + message, cause, driver);
    }
}
