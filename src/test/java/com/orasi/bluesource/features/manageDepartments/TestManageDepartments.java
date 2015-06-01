package com.orasi.bluesource.features.manageDepartments;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.orasi.apps.bluesource.DepartmentsPage;
import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.NewDeptPage;
import com.orasi.apps.bluesource.TopNavigationBar;
import com.orasi.utils.Constants;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class TestManageDepartments  extends TestEnvironment {

    private String application = "";
    
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
	setApplicationUnderTest("Bluesource");
	setBrowserUnderTest(browserUnderTest);
	setBrowserVersion(browserVersion);
	setOperatingSystem(operatingSystem);
	setRunLocation(runLocation);
	setTestEnvironment(environment);
    }

    @AfterMethod(groups = { "regression" })
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
    @Features("Manage Departments")
    @Stories("Given when I login as an admin role, I can add and delete Departments")
    @Severity(SeverityLevel.BLOCKER)
    @Title("Manage Departments")
    @Test(dataProvider = "dataScenario", groups = { "regression" })
    public void testManageDept(@Parameter String testScenario, @Parameter String role,
	    @Parameter String newDept) {
	
	setTestName( new Object() {
	}.getClass().getEnclosingMethod().getName());

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	// Navigate to the dept page
	topNavigationBar.clickAdminLink();
	topNavigationBar.clickDepartmentsLink();

	// Verify navigated to the dept page
	DepartmentsPage deptPage = new DepartmentsPage(this);
	TestReporter.assertTrue(deptPage.pageLoaded(),"Verify list of departments page is displayed");

	// Add a new dept
	deptPage.clickAddDeptLink();
	NewDeptPage newDeptPage = new NewDeptPage(this);
	TestReporter.assertTrue(newDeptPage.pageLoaded(), "Verify add new department page is displayed");
	newDeptPage.CreateNewDept(newDept);

	// Verify the dept is added
	TestReporter.assertTrue(deptPage.isSuccessMsgDisplayed(), "Validate success message appears");
	TestReporter.log("New Dept was created: " + newDept);

	// Verify the dept is displayed on the dept results table
	TestReporter.assertTrue(deptPage.searchTableByDept(newDept), "Validate new department exists in table");

	// Delete the new dept
	deptPage.deleteDept(newDept);

	// Verify the title is deleted
	DepartmentsPage refreshedPage = new DepartmentsPage(this);
	TestReporter.assertTrue(refreshedPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// logout
	topNavigationBar.clickLogout();
    }
}