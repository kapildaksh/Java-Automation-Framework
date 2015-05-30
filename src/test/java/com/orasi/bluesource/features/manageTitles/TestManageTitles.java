package com.orasi.bluesource.features.manageTitles;

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

import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.orasi.utils.Base64Coder;
import com.orasi.utils.Constants;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.Screenshot;
import com.orasi.utils.WebDriverSetup;
import com.orasi.utils.dataProviders.CSVDataProvider;
import com.orasi.utils.dataProviders.ExcelDataProvider;
import com.orasi.apps.bluesource.ListingTitlesPage;
import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.NewTitlePage;
import com.orasi.apps.bluesource.TopNavigationBar;

public class TestManageTitles extends TestEnvironment{

    private String application = "";
    
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
	setApplicationUnderTest("Bluesource");
	setBrowserUnderTest(browserUnderTest);
	setBrowserVersion(browserVersion);
	setOperatingSystem(operatingSystem);
	setRunLocation(runLocation);
	setTestEnvironment(environment);
    }
    
    @AfterMethod(groups = { "regression" })
    public synchronized void closeSession(){
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
    @Features("Manage Titles")
    @Stories("Given when I login as an admin role, I can add and delete Titles")
    @Title("Manage Titles")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dataProvider = "dataScenario", groups = { "regression" })
    public void testManageTitle(@Parameter String testScenario, @Parameter String role,
	    @Parameter String newTitle) {
	testName = new Object() {
	}.getClass().getEnclosingMethod().getName();
	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(), "Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	// Navigate to the title page
	topNavigationBar.clickAdminLink();
	topNavigationBar.clickTitlesLink();

	// Verify navigated to the title page
	ListingTitlesPage listingTitlesPage = new ListingTitlesPage(this);
	TestReporter.assertTrue(listingTitlesPage.pageLoaded(),"Verify listing titles page is displayed");

	// Click new title
	listingTitlesPage.clickNewTitle();

	// Instantiate the New titles page and create a new title
	NewTitlePage newTitlePage = new NewTitlePage(this);
	TestReporter.assertTrue(newTitlePage.pageLoaded(),"Verify create new title page is displayed");
	newTitlePage.createNewTitle(newTitle);

	// Verify the title was created
	TestReporter.assertTrue(listingTitlesPage.isSuccessMsgDisplayed(), "Validate success message appears");
	TestReporter.log("New Title was created: " + newTitle);

	// Verify the title is displayed on the title results table
	TestReporter.assertTrue(listingTitlesPage.searchTableByTitle(newTitle), "Validate new title appears in table");

	// Delete the new title
	listingTitlesPage.deleteTitle(newTitle);

	// Verify the title is deleted
	ListingTitlesPage refreshedPage = new ListingTitlesPage(this);
	TestReporter.assertTrue(refreshedPage.isSuccessMsgDisplayed(), "Validate success message appears");

	// logout
	topNavigationBar.clickLogout();

    }

}