package com.orasi.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

public class Screenshot extends TestListenerAdapter {
	private int Count = 0;
	
	//Constructor
	public Screenshot(){
		
	}
		
	/**
	 * Uses the selenium TakesScreenshot to take a screenshot.  If the folders do not exist
	 * in the project structure to store the screenshots, then it will create them 
	 * (test-output/screenshots).  Uses the Augementer driver so that it call take screenshots
	 * locally & remotely on selenium grid.  
	 * 
	 * @param	None
	 * @version	12/16/2014
	 * @author 	Justin Phlegar
	 * @return 	Nothing
	 */
	
	@Override
    public void onTestFailure(ITestResult testResult) {
		
		try {
			takeScreenShot(testResult);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
    public void onTestSkipped(ITestResult testResult) {
	// will be called after test will be skipped
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
	// will be called after test will pass
    }
	
	public void takeScreenShot(ITestResult testResult) throws IOException {
				
		Object currentClass = testResult.getInstance();
		WebDriver driver = ((WebDriverSetup) currentClass).getDriver();
		String runLocation = ((WebDriverSetup) currentClass).getRunLocation();
		
		//if test is being run on remote machines, driver will need to be instantiated
		if (runLocation.equalsIgnoreCase("remote")) {
			 driver = new Augmenter().augment(driver);
		}
		
		String NewFileNamePath;

		//Get the dir path
		File directory = new File (".");
		
		//get current date time with Date() to create unique file name
		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");
		
		//get current date time with Date()
		Date date = new Date();
		
		//To identify the system
		InetAddress ownIP=InetAddress.getLocalHost();
		File dirScreenshots = new File(directory.getCanonicalPath()+ "\\test-output\\");//");
		
		// if the directory for test output does not exist, then create it
		if (!dirScreenshots.exists()) {

			try {
				dirScreenshots.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}       
		}
		
		// if the directory for the screenshots folder does not exist, then create it
		dirScreenshots = new File(directory.getCanonicalPath()+ "\\test-output\\screenshots\\");
		if (!dirScreenshots.exists()) {
			try {
				dirScreenshots.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}        
		}
		
		//if the directory for the test name folder does not exist, then create it
		dirScreenshots = new File(directory.getCanonicalPath()+ "\\test-output\\screenShots\\" +  testResult.getName() );
		if (!dirScreenshots.exists()) {
			try {
				dirScreenshots.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}    
		}
		
		//create file name using path and current date/time & ip address
		NewFileNamePath = directory.getCanonicalPath()+ "\\test-output\\screenShots\\" +  testResult.getName() +"\\" + dateFormat.format(date) + "_" + ownIP.getHostAddress() + ".png";
		System.out.println(NewFileNamePath);
		
		//Take the screenshot
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); 
		
		try {
			FileUtils.copyFile(scrFile, new File(NewFileNamePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Count++;//Assign each screen shot a number
		NewFileNamePath = "<img src=\"file:///" + NewFileNamePath + "\" alt=\"\"/ ><br />";
		
		//Place the reference in TestNG web report 
		Reporter.log("<br /> <br /> Failed step screenshot <br /> <br /> ");
		Reporter.log(NewFileNamePath);
		
	} 
	
}