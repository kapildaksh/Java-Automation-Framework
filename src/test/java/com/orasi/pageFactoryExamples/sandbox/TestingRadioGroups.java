package com.orasi.pageFactoryExamples.sandbox;

import java.util.HashMap;
import java.util.Map;

import javax.naming.directory.NoSuchAttributeException;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.apps.sandbox.RadioGroupsTest;
import com.orasi.core.interfaces.RadioGroup;
import com.orasi.utils.WebDriverSetup;

public class TestingRadioGroups {

	private String application = "";
	private String browserUnderTest = "";
	private String browserVersion = "";
	private String operatingSystem = "";
	private String runLocation = "";
	private String environment = "";
	private Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();

	@BeforeTest()
	@Parameters({ "runLocation", "browserUnderTest", "browserVersion",
			"operatingSystem", "environment" })
	public void setup(@Optional String runLocation, String browserUnderTest,
			@Optional String browserVersion, @Optional String operatingSystem,
			String environment) {
		this.application = "RADIOGROUPTESTING";
		this.runLocation = runLocation;
		this.browserUnderTest = browserUnderTest;
		this.browserVersion = browserVersion;
		this.operatingSystem = operatingSystem;
		this.environment = environment;

	}

	@AfterMethod(groups = { "regression", "housekeeping" })
	public synchronized void closeSession(ITestResult test) {
		System.out.println(test.getMethod().getMethodName());
		if(drivers.size() != 0){
			WebDriver driver = drivers.get(test.getMethod().getMethodName());
			if(driver != null){
				driver.quit();
			}
		}
	}

	/**
	 * @throws NoSuchAttributeException 
	 * @Summary: Adds a housekeeper to the schedule
	 * @Precondition:NA
	 * @Author: Jessica Marshall
	 * @Version: 10/6/2014
	 * @Return: N/A
	 */
	// @Test(dataProvider = "dataScenario")
	@Test
	public void testRadioGroups() throws NoSuchAttributeException {

		String testName = new Object() {
		}.getClass().getEnclosingMethod().getName();

		WebDriverSetup setup = new WebDriverSetup(application,
				browserUnderTest, browserVersion, operatingSystem, runLocation,
				environment);
		WebDriver driver = setup.initialize();
		drivers.put(testName, driver);
		
		RadioGroupsTest test = new RadioGroupsTest(driver);
		Assert.assertEquals(test.pageLoaded(), true, "Page was not loaded");
		test.testInteractions();
	}
}
