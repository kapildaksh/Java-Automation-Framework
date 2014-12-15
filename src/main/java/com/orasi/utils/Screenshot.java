package com.orasi.utils;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

public class Screenshot extends TestListenerAdapter {
	private int Count = 0;
	
	@Override
	public void onTestStart(ITestResult testResult){
		System.out.println("Start test: " + testResult.getInstanceName());
	}
	
	//Take screen shot only for failed test case
	@Override
	public void onTestFailure(ITestResult testResult) {
	//ScreenShot(testResult);
		System.out.println("Test " + testResult.getInstanceName() + " failed.");
		System.out.println();
	}
	
	@Override
	public void onTestSkipped(ITestResult testResult) {
		System.out.println("Test " + testResult.getInstanceName() + " skipped.");
		System.out.println();
	}
	
	@Override
	public void onTestSuccess(ITestResult testResult) {
		System.out.println("Test " + testResult.getInstanceName() + " passed.");
		System.out.println();
	}
	
	private void ScreenShot(ITestResult testResult) {
		try {
		
		String NewFileNamePath;
		
		
		//Code to get screen resolution
		//Get the default toolkit
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		
		//Get the current screen size
		Dimension scrnsize = toolkit.getScreenSize();		 		
		
		//Get the dir path
		File directory = new File (".");
		
		//get current date time with Date() to create unique file name
		DateFormat dateFormat = new SimpleDateFormat("dd_MMM_yyyy__hh_mm_ssaa");
		
		//get current date time with Date()
		Date date = new Date();
		
		//To identify the system
		InetAddress ownIP=InetAddress.getLocalHost();
		File dirScreenshots = new File(directory.getCanonicalPath()+ "\\test-output\\");//");
		
		// if the directory does not exist, create it
		if (!dirScreenshots.exists()) {
		  boolean result = false;
		  try{
			  dirScreenshots.mkdir();
		      result = true;
		   } catch(SecurityException se){
		      //handle it
		   }        
		}
		
		dirScreenshots = new File(directory.getCanonicalPath()+ "\\test-output\\screenshots\\");
		// if the directory does not exist, create it
		if (!dirScreenshots.exists()) {
		  boolean result = false;
		  try{
			  dirScreenshots.mkdir();
		      result = true;
		   } catch(SecurityException se){
		      //handle it
		   }        
		}
		
		dirScreenshots = new File(directory.getCanonicalPath()+ "\\test-output\\screenShots\\" +  testResult.getName() );
		if (!dirScreenshots.exists()) {
			  boolean result = false;
			  try{
				  dirScreenshots.mkdir();
			      result = true;
			   } catch(SecurityException se){
			      //handle it
			   }        
			}
		NewFileNamePath = directory.getCanonicalPath()+ "\\test-output\\screenShots\\" +  testResult.getName() +"\\" + dateFormat.format(date) + "_" + ownIP.getHostAddress() + ".png";
		System.out.println(NewFileNamePath);
		
		//Capture the screen shot of the area of the screen defined by the rectangle
		Robot robot = new Robot();
		
		//BufferedImage bi=robot.createScreenCapture(new Rectangle(1280,1024));
		BufferedImage bi=robot.createScreenCapture(new Rectangle(scrnsize));
		ImageIO.write(bi, "png", new File(NewFileNamePath));
		
		Count++;//Assign each screen shot a number
		NewFileNamePath = "<img src=\"file:///" + NewFileNamePath + "\" alt=\"\"/><br />";
		
		//Place the reference in TestNG web report 
		Reporter.log("<br /> <br /> Failed step screenshot <br /> <br /> ");
		Reporter.log(NewFileNamePath);
		
		
		} 
		catch (AWTException e) {
		e.printStackTrace();
		} 
		catch (IOException e) {
		e.printStackTrace();
		} catch (NoSuchElementException e){
			
		}
	}
}
