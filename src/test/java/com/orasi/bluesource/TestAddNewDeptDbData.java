package com.orasi.bluesource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
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
import com.orasi.utils.dataProviders.DatabaseDataProvider;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.orasi.apps.bluesource.DepartmentsPage;
import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.NewDeptPage;
import com.orasi.apps.bluesource.TopNavigationBar;

public class TestAddNewDeptDbData {
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
    	Object[][] dbData = new DatabaseDataProvider(DatabaseDataProvider.MYSQL).getTestData("TestAddNewDepartment");
		OrasiReporter.testCount = dbData.length;
		return dbData;
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
		htmlReport.ReportEvent("Stop",test.getMethod().getMethodName(), null, false);

		// if is a failure, then take a screenshot
		if (test.getStatus() == ITestResult.FAILURE) {
			new Screenshot().takeScreenShot(test, driver);
		}
		driver.quit();
	}
	@AfterSuite
	public void outputHTML(ITestContext ctx){
		htmlReport.GenerateHTML("1_testCreateNewDept,2_testCreateNewDept,3_testCreateNewDept,4_testCreateNewDept", ctx.getCurrentXmlTest().getSuite().getName());
	}

    /**
     * @throws IOException 
     * @throws InterruptedException 
     * @throws Exception
     * @Summary: Adds a housekeeper to the schedule
     * @Precondition:NA
     * @Author: Jessica Marshall
     * @Version: 10/6/2014
     * @Return: N/A
     */
    @Test(dataProvider = "dataScenario", groups = { "regression" })
    public void testCreateNewDept(String testScenario, String role,
	    String newDept) throws InterruptedException, IOException {
	
	String testName = new Object(){}.getClass().getEnclosingMethod().getName();

	//WebDriverSetup setup = new WebDriverSetup(application,
	//	browserUnderTest, browserVersion, operatingSystem, runLocation,
	//	environment);
	//WebDriver driver = setup.initialize();
	WebDriver driver = WebDriverSetup.getDriver();
	//drivers.put(testName, driver);
	
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