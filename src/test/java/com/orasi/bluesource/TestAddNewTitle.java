package com.orasi.pageFactoryExamples.bluesource;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.utils.Constants;
import com.orasi.utils.TestReporter;
import com.orasi.utils.Screenshot;
import com.orasi.utils.WebDriverSetup;
import com.orasi.utils.dataProviders.CSVDataProvider;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.orasi.apps.bluesource.ListingTitlesPage;
import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.NewTitlePage;
import com.orasi.apps.bluesource.TopNavigationBar;

public class TestAddNewTitle {

    private String application = "";
    private String browserUnderTest = "";
    private String browserVersion = "";
    private String operatingSystem = "";
    private String runLocation = "";
    private String environment = "";
    private Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();

    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH
		+ "TestAddNewTitle.xlsx", "TestAddNewTitle").getTestData();
    }

    @BeforeTest(groups = { "regression" })
    @Parameters({ "runLocation", "browserUnderTest", "browserVersion",
	    "operatingSystem", "environment" })
    public void setup(@Optional String runLocation, String browserUnderTest,
	    String browserVersion, String operatingSystem, String environment) {
	this.application = "Bluesource";
	this.runLocation = runLocation;
	this.browserUnderTest = browserUnderTest;
	this.browserVersion = browserVersion;
	this.operatingSystem = operatingSystem;
	this.environment = environment;

    }
    
    @AfterMethod(groups = { "regression" })
    public synchronized void closeSession(ITestResult test){
	System.out.println(test.getMethod().getMethodName());
	WebDriver driver = drivers.get(test.getMethod().getMethodName());   
	
	//if is a failure, then take a screenshot
	if (test.getStatus() == ITestResult.FAILURE){
		new Screenshot().takeScreenShot(test, driver);
	}
	driver.quit();
    }

    /**
     * @throws Exception
     * @Summary: Adds a housekeeper to the schedule
     * @Precondition:NA
     * @Author: Jessica Marshall
     * @Version: 10/6/2014
     * @Return: N/A
     */
    @Test(dataProvider = "dataScenario", groups = { "regression" })
    public void testCreateNewTitle(String testScenario, String role,
	    String newTitle) {

	String testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	WebDriverSetup setup = new WebDriverSetup(application,
		browserUnderTest, browserVersion, operatingSystem, runLocation,
		environment);
	WebDriver driver = setup.initialize();
	System.out.println(testName);
	drivers.put(testName, driver);

	// Login
	LoginPage loginPage = new LoginPage(driver);
	Assert.assertTrue(loginPage.pageLoaded(),
		"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	Assert.assertTrue(topNavigationBar.isLoggedIn());
	TestReporter.log("User was logged in successfully");

	// Navigate to the title page
	topNavigationBar.clickAdminLink();
	topNavigationBar.clickTitlesLink();

	// Verify navigated to the title page
	ListingTitlesPage listingTitlesPage = new ListingTitlesPage(driver);
	Assert.assertTrue(listingTitlesPage.pageLoaded(),
		"Verify listing titles page is displayed");
	TestReporter.log("Navigated to the listing titles page");

	// Click new title
	listingTitlesPage.clickNewTitle();
	TestReporter.log("Navigated to the new title page");

	// Instantiate the New titles page and create a new title
	NewTitlePage newTitlePage = new NewTitlePage(driver);
	Assert.assertTrue(newTitlePage.pageLoaded(),
		"Verify create new title page is displayed");
	newTitlePage.createNewTitle(newTitle);

	// Verify the title was created
	Assert.assertTrue(listingTitlesPage.isSuccessMsgDisplayed());
	TestReporter.log("New Title was created: " + newTitle);

	// Verify the title is displayed on the title results table
	Assert.assertTrue(listingTitlesPage.searchTableByTitle(newTitle));
	TestReporter.log("New title was found in table of titles");

	// Delete the new title
	listingTitlesPage.deleteTitle(newTitle);

	// Verify the title is deleted
	ListingTitlesPage refreshedPage = new ListingTitlesPage(driver);
	Assert.assertTrue(refreshedPage.isSuccessMsgDisplayed());
	TestReporter.log("New title was deleted successfully");

	// logout
	topNavigationBar.logout();

    }

}