package com.orasi.bluesource.features.manageEmployees;

import java.util.HashMap;
import java.util.Map;

import junit.extensions.TestSetup;

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

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;

import com.orasi.utils.Constants;
import com.orasi.utils.Sleeper;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.Screenshot;
import com.orasi.utils.WebDriverSetup;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.orasi.apps.bluesource.DepartmentsPage;
import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.NewDeptPage;
import com.orasi.apps.bluesource.TopNavigationBar;
import com.orasi.apps.bluesource.employeesPage.EmployeesPage;

public class ShowEmployeesPerPage  extends TestEnvironment {

    private String application = "";
    
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH
		+ "ManageEmployees.xlsx", "ShowEmployeesPerPage").getTestData();
    }

    @BeforeTest(groups = { "regression", "manageEmployees", "showEmployeesPerPage" })
    @Parameters({ "runLocation", "browserUnderTest", "browserVersion",
	    "operatingSystem", "environment" })
    public void setup(@Optional String runLocation, String browserUnderTest,
	    String browserVersion, String operatingSystem, String environment) {
	setApplicationUnderTest("Bluesource");
	setBrowserUnderTest(browserUnderTest);
	setBrowserVersion(browserVersion);
	setOperatingSystem(operatingSystem);
	setRunLocation(runLocation);
	setTestEnvironment(environment);
    }

    @AfterMethod(groups = { "regression", "manageEmployees", "showEmployeesPerPage" })
    public synchronized void closeSession(ITestResult test) {
	endTest(testName);
    }

    /**
     * @throws Exception
     * @Summary: Adds a housekeeper to the schedule
     * @Precondition:NA
     * @Author: Jessica Marshall
     * @Version: 10/6/2014
     * @Return: N/A
     */
    @Features("ManageEmployees")
    @Stories("I can select the number of Employees that is displayed")
    @Title("ShowEmployeesPerPage")
    @Test(dataProvider = "dataScenario", groups = { "regression", "manageEmployees", "showEmployeesPerPage" })
    public void testShowEmployeesPerPage(@Parameter String testScenario, @Parameter String role, @Parameter String numberOfRows) {
	
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Employees Page
	topNavigationBar.clickEmployeesLink();
	EmployeesPage employeesPage = new EmployeesPage(this);
	TestReporter.assertTrue(employeesPage.pageLoaded(),"Verify Employees page is displayed");
	
	//Search for Employee
	employeesPage.setRowsPerPageDisplayed(numberOfRows);
	TestReporter.assertTrue(employeesPage.validateRowsPerPageDisplayed(numberOfRows), "Validate " + numberOfRows + " were displayed on the Employee Table");

	// logout
	topNavigationBar.clickLogout();
    }
    
}