package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class FormRegexValidationException extends AutomationException {
    
    private static final long serialVersionUID = 1L;

    public FormRegexValidationException() {
        super("Detected a form regex-validation error message");
    }
    
    public FormRegexValidationException(String message) {
        super("Detected a form regex-validation error message: " + message);
    }
    
    public FormRegexValidationException(String message, Throwable cause) {
        super("Detected a form regex-validation error message: " + message, cause);
    }
    
    public FormRegexValidationException(WebDriver driver) {
        super("Detected a form regex-validation error message: ", driver);
    }
    
    public FormRegexValidationException(String message, WebDriver driver) {
        super("Detected a form regex-validation error message: " + message);
    }
    
    public FormRegexValidationException(String message, Throwable cause, WebDriver driver) {
        super("Detected a form regex-validation error message: " + message, cause, driver);
    }
}
