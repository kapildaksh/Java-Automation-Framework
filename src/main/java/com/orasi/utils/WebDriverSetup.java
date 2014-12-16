
package com.orasi.utils;

import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;


public class WebDriverSetup {


	public WebDriver driver;
	private String testEnvironment = "";
	private String testApplication = "";
	private String driverWindow = "";
	private String operatingSystem = "";
	private String browserVersion = "";
	private String browser = null;
	private String location = null;
	private ResourceBundle appURLRepository = ResourceBundle.getBundle(Constants.ENVIRONMENT_URL_PATH);
	private URL seleniumHubURL = null;
		
	public WebDriverSetup(){}
	

	public WebDriverSetup(	String application, String browserUnderTest, 
							String browserVersion, String operatingSystem,
							String runLocation, String environment){

		
		this.testApplication = application;
		this.browser = browserUnderTest;
		this.browserVersion = browserVersion;
		this.operatingSystem = operatingSystem;
		this.location = runLocation;
		this.testEnvironment = environment;

	}
	
	//Getters & Setters
	
	public void setTestEnvironment(String environment){
		testEnvironment = environment;
	}
	
	public  String getTestEnvironment(){
		return testEnvironment;
	}

	
	public  void setTestApplication(String application){
		testApplication= application;
	}

	public String getTestApplication(){
		return testApplication;
	}

	public void setDriverWindow(String window){
		driverWindow= window;
	}

	public String getDriverWindow(){
		return driverWindow;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}


	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public void setBrowserUnderTest(String browser) {
		this.browser = browser;
	}
	
	public String getBrowserUnderTest(){
		return browser;
	}
	
	public String getBrowserVersion() {
		return browserVersion;
	}


	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}
	
	public  ResourceBundle getEnvironmentURLRepository(){
		return appURLRepository;
	}

	public static void setDefaultTestTimeout(int timeout){
		System.setProperty(Constants.TEST_DRIVER_TIMEOUT, Integer.toString(timeout));
	}
	
	public static int getDefaultTestTimeout(){
		return Integer.parseInt(System.getProperty(Constants.TEST_DRIVER_TIMEOUT));
	}
	
	public void setDriver(WebDriver driverSession){
		driver = driverSession;
	}
	
	public WebDriver getDriver(){
		return driver;
	}
	
	/**
	 * Initializes the webdriver, sets up the run location, driver type,
	 * launches the application.
	 * 
	 * @param	None
	 * @version	12/16/2014
	 * @author 	Jessica Marshall
	 * @return 	the web driver
	 */
	public WebDriver initialize(){
		
		driverSetup();
		launchApplication();
		return this.driver;
	}
	
	
	/**
	 * Launches the application under test.  Gets the URL from an environment properties file
	 * based on the application.  
	 * 
	 * @param	None
	 * @version	12/16/2014
	 * @author 	Justin Phlegar
	 * @return 	Nothing
	 */
	public void launchApplication(){
		driver.get(appURLRepository.getString(testApplication.toUpperCase() + "_" + testEnvironment.toUpperCase()));
		
	}
	
	/**
	 * Sets up the driver type, location, browser under test 
	 * 
	 * @param	None
	 * @version	12/16/2014
	 * @author 	Justin Phlegar
	 * @return 	Nothing 
	 */
	public void driverSetup(){

		//Set the URL for selenium grid
		try {
			seleniumHubURL = new URL(Constants.SELENIUM_HUB_URL);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Selenium Hub URL set is not a valid URL: " + seleniumHubURL);
		}

		driver = null;

		//If the location is local, grab the drivers for each browser type from within the project
		if (location.equalsIgnoreCase("local")){
			//firefox
			if (browser.equalsIgnoreCase("Firefox")){
		    	driver = new FirefoxDriver();	    	
		    }
			//Internet explorer
		    else if(browser.equalsIgnoreCase("IE")){
		    	DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
		    	caps.setCapability("ignoreZoomSetting", true);
		    	caps.setCapability("enablePersistentHover", false);
		    	
		    	//File file = new File("C:\\Selenium\\WebDrivers\\IEDriverServer.exe");
		    	File file = new File(WebDriverSetup.class.getResource(Constants.DRIVERS_PATH_LOCAL + "IEDriverServer.exe").getPath());
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				driver = new InternetExplorerDriver(caps);
		    }
			//Chrome
		    else if(browser.equalsIgnoreCase("Chrome")){
		    	//File file = new File("C:\\Selenium\\WebDrivers\\ChromeDriver.exe");
		    	File file = new File(WebDriverSetup.class.getResource(Constants.DRIVERS_PATH_LOCAL + "ChromeDriver.exe").getPath());
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
				driver = new ChromeDriver();		    	
		    }
			//Headless - HTML unit driver
		    else if(browser.equalsIgnoreCase("html")){	    	
				driver = new HtmlUnitDriver(true);		    	
		    }
			//Safari
		    else if(browser.equalsIgnoreCase("safari")){
		    	//TODO - Enter code for this
		    }
		    else {
		    	throw new RuntimeException("Parameter not set for browser type");
		    }
		
		//Code for running on the selenium grid
		}else if(location.equalsIgnoreCase("remote")){
			//firefox
			if (browser.equalsIgnoreCase("Firefox")){
				DesiredCapabilities caps = DesiredCapabilities.firefox();
				caps.setPlatform(org.openqa.selenium.Platform.WINDOWS);
		    	driver = new RemoteWebDriver(seleniumHubURL, caps);	    	
		    }
			//internet explorer
		    else if(browser.equalsIgnoreCase("IE")){
		    	DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
		    	caps.setCapability("ignoreZoomSetting", true);
		    	caps.setPlatform(org.openqa.selenium.Platform.WINDOWS);		    	
				driver = new RemoteWebDriver(seleniumHubURL, caps);	
		    }
			//chrome
		    else if(browser.equalsIgnoreCase("Chrome")){
		    	DesiredCapabilities caps = DesiredCapabilities.chrome();
				caps.setPlatform(org.openqa.selenium.Platform.WINDOWS);
		    	driver = new RemoteWebDriver(seleniumHubURL, caps);	    		    	
		    }
			//headless - HTML unit driver
		    else if(browser.equalsIgnoreCase("html")){	    	
				driver = new HtmlUnitDriver(true);		    	
		    }
		    else if(browser.equals("safari")){
		    	//TODO need code for this
		    }
		    else {
		    	throw new RuntimeException("Parameter not set for browser type");
		    }
		}else{
			throw new RuntimeException("Parameter for run [Location] was not set to 'Local' or 'Remote'");
		}

		driver.manage().timeouts().setScriptTimeout(Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT, TimeUnit.SECONDS).implicitlyWait(Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT, TimeUnit.SECONDS);	
		//driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		setDriverWindow(driver.getWindowHandle());

	}



}
