package com.orasi.utils.exception.uimessage;

import org.openqa.selenium.WebDriver;

import com.orasi.utils.exception.automation.WebElementNotDisappearedException;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class WebUiModalNotDisappearedException extends WebElementNotDisappearedException {

    private static final long serialVersionUID = 1L;

    public WebUiModalNotDisappearedException() {
        super("Modal has not disappeared");
    }
    
    public WebUiModalNotDisappearedException(String message) {
        super("Modal has not disappeared: " + message);
    }
    
    public WebUiModalNotDisappearedException(String message, Throwable cause) {
        super("Payment modal not found: " + message, cause);
    }
    
    public WebUiModalNotDisappearedException(WebDriver driver) {
        super("Modal has not disappeared: ", driver);
    }
    
    public WebUiModalNotDisappearedException(String message, WebDriver driver) {
        super("Modal has not disappeared: " + message, driver);
    }
    
    public WebUiModalNotDisappearedException(String message, Throwable cause, WebDriver driver) {
        super("Modal has not disappeared: " + message, cause, driver);
    }
}
