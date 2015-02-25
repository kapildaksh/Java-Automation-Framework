package com.orasi.bluesource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.reporting.OrasiReporter;
import com.orasi.utils.Constants;
import com.orasi.utils.TestReporter;
import com.orasi.utils.Screenshot;
import com.orasi.utils.WebDriverSetup;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.orasi.apps.bluesource.ListingTitlesPage;
import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.NewTitlePage;
import com.orasi.apps.bluesource.TopNavigationBar;

public class TestAddNewTitle {
	OrasiReporter htmlReport = new OrasiReporter();
	private String application = "";
	private String browserUnderTest = "";
	private String browserVersion = "";
	private String operatingSystem = "";
	private String runLocation = "";
	private String environment = "";
	private int dataIterations = 0;
	private Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();

	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		Object[][] excelData = new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH + "TestAddNewTitle.xlsx", "TestAddNewTitle").getTestData();
		OrasiReporter.testCount = excelData.length;
		return excelData;
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
	@BeforeMethod(groups = { "regression" })
	protected void startReport(Method method) throws InterruptedException, IOException{
		String testName = method.getName(); 
		WebDriverSetup setup = new WebDriverSetup(application,
				browserUnderTest, browserVersion, operatingSystem, runLocation,
				environment);
		WebDriver driver = setup.initialize();
		drivers.put(testName, driver);
		htmlReport.ReportEvent("Start", null, testName, false);

	}
	@AfterMethod(groups = { "regression" })
	public synchronized void closeSession(ITestResult test) {
		System.out.println(test.getMethod().getMethodName());
		WebDriver driver = drivers.get(test.getMethod().getMethodName());
		htmlReport.ReportEvent("Stop",test.getMethod().getMethodName(), test.getMethod().getMethodName() + " Complete", false);

		// if is a failure, then take a screenshot
		if (test.getStatus() == ITestResult.FAILURE) {
			new Screenshot().takeScreenShot(test, driver);
		}
		driver.quit();
	}
	@AfterSuite
	public void outputHTML(ITestContext ctx){
		htmlReport.GenerateHTML("1_testCreateNewTitle", ctx.getCurrentXmlTest().getSuite().getName());
	}

	/**
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws Exception
	 * @Summary: Adds and deletes a title to the Orasi Blue Source website
	 * @Precondition:NA
	 * @Author: Jessica Marshall
	 * @Version: 10/6/2014
	 * @Return: N/A
	 */
	@Test(dataProvider = "dataScenario", groups = { "regression" })
	public void testCreateNewTitle(String testScenario, String role,
			String newTitle) throws InterruptedException, IOException {
		String testName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		WebDriver driver = WebDriverSetup.getDriver();
		System.out.println(testName);

		// Login
		LoginPage loginPage = new LoginPage(driver);
		//TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
		if(loginPage.pageLoaded()){
			htmlReport.ReportEvent("Pass", "Login Page Loaded", "The login page was loaded successfully.", false);
		}else{
			htmlReport.ReportEvent("Fail", "Login Page Not Loaded", "The login page failed to load successfully.", true);
		}
		loginPage.login(role);

		// Verify user is logged in
		TopNavigationBar topNavigationBar = new TopNavigationBar(driver);
		//TestReporter.assertTrue(topNavigationBar.isLoggedIn(),"Validate the user logged in successfully");
		if(topNavigationBar.isLoggedIn()){
			htmlReport.ReportEvent("Pass", "User Logged In", "The user logged in successfully.", false);
		}else{
			htmlReport.ReportEvent("Fail", "User Login Failed", "The user failed to login successfully.", true);
		}
		// Navigate to the title page
		topNavigationBar.clickAdminLink();
		topNavigationBar.clickTitlesLink();

		// Verify navigated to the title page
		ListingTitlesPage listingTitlesPage = new ListingTitlesPage(driver);
		//TestReporter.assertTrue(listingTitlesPage.pageLoaded(),"Verify listing titles page is displayed");
		if(listingTitlesPage.pageLoaded()){
			htmlReport.ReportEvent("Pass", "Title Page Loaded", "The title page loaded successfully.", false);
		}else{
			htmlReport.ReportEvent("Fail", "Title Page Failed to Load", "The title page failed to load successfully.", true);
		}

		// Click new title
		listingTitlesPage.clickNewTitle();

		// Instantiate the New titles page and create a new title
		NewTitlePage newTitlePage = new NewTitlePage(driver);
		//TestReporter.assertTrue(newTitlePage.pageLoaded(),"Verify create new title page is displayed");
		if(newTitlePage.pageLoaded()){
			htmlReport.ReportEvent("Pass", "New Title Page Loaded", "The new title page loaded successfully.", false);
		}else{
			htmlReport.ReportEvent("Fail", "New Title Page Failed to Load", "The new title page failed to load successfully.", true);
		}
		newTitlePage.createNewTitle(newTitle);

		// Verify the title was created
		//TestReporter.assertTrue(listingTitlesPage.isSuccessMsgDisplayed(),"Validate success message appears");
		if(listingTitlesPage.isSuccessMsgDisplayed()){
			htmlReport.ReportEvent("Pass", "New Title Created", "The new title [" +newTitle+ "] was created successfully.", false);
		}else{
			htmlReport.ReportEvent("Fail", "New Title Failed to Create", "The new title [" +newTitle+ "] was not created successfully.", true);
		}
		TestReporter.log("New Title was created: " + newTitle);

		// Verify the title is displayed on the title results table
		//TestReporter.assertTrue(listingTitlesPage.searchTableByTitle(newTitle),"Validate new title appears in table");
		if(listingTitlesPage.searchTableByTitle(newTitle)){
			htmlReport.ReportEvent("Pass", "New Title Found", "The new title [" +newTitle+ "] was found in the table as expected.", false);
		}else{
			htmlReport.ReportEvent("Fail", "New Title Not Found", "The new title [" +newTitle+ "] was not found in the table as expected.", true);
		}
		// Delete the new title
		listingTitlesPage.deleteTitle(newTitle);

		// Verify the title is deleted
		ListingTitlesPage refreshedPage = new ListingTitlesPage(driver);
		//TestReporter.assertTrue(refreshedPage.isSuccessMsgDisplayed(),"Validate success message appears");
		if(refreshedPage.isSuccessMsgDisplayed()){
			htmlReport.ReportEvent("Pass", "New Title Deleted", "The new title [" +newTitle+ "] was deleted from the table as expected.", false);
		}else{
			htmlReport.ReportEvent("Fail", "New Title Not Deleted", "The new title [" +newTitle+ "] was not deleted from the table as expected.", true);
		}
		if(!listingTitlesPage.searchTableByTitle(newTitle)){
			htmlReport.ReportEvent("Pass", "New Title Removed from Table", "The new title [" +newTitle+ "] was not found in the table, as expected.", false);
		}else{
			htmlReport.ReportEvent("Fail", "New Title Not Removed from Table", "The new title [" +newTitle+ "] was still found in the table as not expected.", true);
		}
		// logout
		topNavigationBar.logout();

	}
}