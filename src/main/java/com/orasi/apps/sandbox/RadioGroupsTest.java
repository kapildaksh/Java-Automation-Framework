package com.orasi.apps.sandbox;

import javax.naming.directory.NoSuchAttributeException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.orasi.utils.PageLoaded;
import com.orasi.core.interfaces.*;
import com.orasi.core.interfaces.impl.internal.ElementFactory;

public class RadioGroupsTest {
	// *****************
	// ** Page Fields **
	// *****************

	private static WebDriver driver;

	// *******************
	// ** Page Elements **
	// *******************

	@FindBy(xpath = "//*[@id='content']/table[1]")
	//@FindBy(css = "#content > table:nth-child(15)")
	private Webtable tblTable;
	
	@FindBy(xpath = "//*[@id='content']/table[1]/tbody/tr/td[2]/table")
	//@FindBy(css = "#content > table:nth-child(15) > tbody > tr > td:nth-child(2) > table")
	private Webtable newTable;

	// *********************
	// ** Build page area **
	// *********************
	public RadioGroupsTest(WebDriver driver) {
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}

	public boolean pageLoaded() {
		return new PageLoaded().isElementLoaded(this.getClass(), driver, tblTable);
	}

	public boolean pageLoaded(Element element) {
		return new PageLoaded().isElementLoaded(this.getClass(), driver,
				element);
	}

	public RadioGroupsTest initialize() {
		return ElementFactory.initElements(driver, this.getClass());
	}

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	public void testInteractions() throws NoSuchAttributeException {
		initialize();
		pageLoaded();
		
		int loopCounter;
		int rowCount;

		long startTime = System.currentTimeMillis();
		rowCount = tblTable.getRowCount(driver);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Table 1 row count: " + String.valueOf(rowCount));
		System.out.println("Time for table 1 row count: " + String.valueOf((float)elapsedTime/1000) + " seconds");
		for(loopCounter = 1; loopCounter <= rowCount; loopCounter++){
			startTime = System.currentTimeMillis();
			System.out.println("Table 1, row "+String.valueOf(loopCounter)+" column count: " + tblTable.getColumnCount(driver, loopCounter));
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			System.out.println("Time for table 1 row "+String.valueOf(loopCounter)+" column count: " + String.valueOf((float)elapsedTime/1000) + " seconds");
		}
		
		startTime = System.currentTimeMillis();
		rowCount = newTable.getRowCount(driver);
		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		System.out.println("Table 2 row count:" + String.valueOf(rowCount));
		System.out.println("Time for table 2 row count: " + String.valueOf((float)elapsedTime/1000) + " seconds");
		for(loopCounter = 1; loopCounter <= rowCount; loopCounter++){
			startTime = System.currentTimeMillis();
			System.out.println("Table 2, row "+String.valueOf(loopCounter)+" column count: " + newTable.getColumnCount(driver, loopCounter));
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;
			System.out.println("Time for table 2 row "+String.valueOf(loopCounter)+" column count: " + String.valueOf((float)elapsedTime/1000) + " seconds");	
		}
	}
}
