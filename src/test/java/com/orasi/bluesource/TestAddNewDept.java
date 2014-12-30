package com.orasi.bluesource;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.utils.Constants;
import com.orasi.utils.TestReporter;
import com.orasi.utils.Screenshot;
import com.orasi.utils.WebDriverSetup;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.orasi.apps.bluesource.DepartmentsPage;
import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.NewDeptPage;
import com.orasi.apps.bluesource.TopNavigationBar;

public class TestAddNewDept {

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
		+ "TestAddNewDept.xlsx", "TestAddNewDept").getTestData();
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
    public synchronized void closeSession(ITestResult test) {
	WebDriver driver = drivers.get(test.getMethod().getMethodName());

	// if is a failure, then take a screenshot
	if (test.getStatus() == ITestResult.FAILURE) {
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
    public void testCreateNewDept(String testScenario, String role,
	    String newDept) {
	
	String testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	WebDriverSetup setup = new WebDriverSetup(application,
		browserUnderTest, browserVersion, operatingSystem, runLocation,
		environment);
	WebDriver driver = setup.initialize();

	// Login
	LoginPage loginPage = new LoginPage(driver);
	TestReporter.assertTrue(loginPage.pageLoaded(),
		"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	// Navigate to the dept page
	topNavigationBar.clickAdminLink();
	topNavigationBar.clickDepartmentsLink();

	// Verify navigated to the dept page
	DepartmentsPage deptPage = new DepartmentsPage(driver);
	TestReporter.assertTrue(deptPage.pageLoaded(),
		"Verify list of departments page is displayed");

	// Add a new dept
	deptPage.clickAddDeptLink();
	NewDeptPage newDeptPage = new NewDeptPage(driver);
	TestReporter.assertTrue(newDeptPage.pageLoaded(),
		"Verify add new department page is displayed");
	newDeptPage.CreateNewDept(newDept);

	// Verify the dept is added
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");
	TestReporter.log("New Dept was created: " + newDept);

	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.searchTableByDept(newDept), "Validate new department exists in table");

	// Delete the new dept
	deptPage.deleteDept(newDept);

	// Verify the title is deleted
	DepartmentsPage refreshedPage = new DepartmentsPage(driver);
	TestReporter.assertTrue(refreshedPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// logout
	topNavigationBar.logout();
    }
}