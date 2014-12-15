package com.orasi.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FrameHandler {
	public static void findAndSwitchToFrame(WebDriver driver, String frame){
		driver.switchTo().defaultContent();
		WebDriverWait wait = new WebDriverWait(driver,WebDriverSetup.getDefaultTestTimeout());
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frame));		
	}
}
