package com.orasi.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

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
			if(this.operatingSystem.equalsIgnoreCase("windows")){
				
			}else if(this.operatingSystem.equalsIgnoreCase("mac")){
				
			}else if(this.operatingSystem.equalsIgnoreCase("linux")){
				
			}
			DesiredCapabilities caps = null;
			File file = null;
			if (browser.equalsIgnoreCase("Firefox")){
		    	driver = new FirefoxDriver();	    	
		    }
			//Internet explorer
		    else if(browser.equalsIgnoreCase("IE")){
		    	caps = DesiredCapabilities.internetExplorer();
		    	caps.setCapability("ignoreZoomSetting", true);
		    	caps.setCapability("enablePersistentHover", false);
		    	file = new File(this.getClass().getResource(Constants.DRIVERS_PATH_LOCAL + "IEDriverServer.exe").getPath());
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				driver = new InternetExplorerDriver(caps);
		    }
			//Chrome
		    else if(browser.equalsIgnoreCase("Chrome")){
		    	file = new File(this.getClass().getResource(Constants.DRIVERS_PATH_LOCAL + "ChromeDriver.exe").getPath());
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
				driver = new ChromeDriver();		    	
		    }
			//Headless - HTML unit driver
		    else if(browser.equalsIgnoreCase("html")){	    	
				driver = new HtmlUnitDriver(true);		    	
		    }
			//Safari
		    else if(browser.equalsIgnoreCase("safari")){
		    	driver = new SafariDriver();
		    }
		    else {
		    	throw new RuntimeException("Parameter not set for browser type");
		    }
		
		//Code for running on the selenium grid
		}else if(location.equalsIgnoreCase("remote")){
			
			DesiredCapabilities caps = null;
			
			//firefox
			if (browser.equalsIgnoreCase("Firefox")){
				caps = DesiredCapabilities.firefox();
				caps.setVersion(browserVersion);
				    	
		    }
			//internet explorer
		    else if(browser.equalsIgnoreCase("IE")){
		    	caps = DesiredCapabilities.internetExplorer();
		    	caps.setCapability("ignoreZoomSetting", true);
		    	caps.setVersion(browserVersion);
		    	
		    }
			//chrome
		    else if(browser.equalsIgnoreCase("Chrome")){
		    	caps = DesiredCapabilities.chrome();
		    	caps.setVersion(browserVersion);
		    	   		    	
		    }
			//headless - HTML unit driver
		    else if(browser.equalsIgnoreCase("html")){	
		    	caps = DesiredCapabilities.htmlUnitWithJs();
		    			    	
		    }
			//safari
		    else if(browser.equals("safari")){
		    	caps = DesiredCapabilities.safari();
		    }
		    else {
		    	throw new RuntimeException("Parameter not set for browser type");
		    }
			
			caps.setPlatform(org.openqa.selenium.Platform.valueOf(operatingSystem));
	    	driver = new RemoteWebDriver(seleniumHubURL, caps);	
	    	
		}else{
			throw new RuntimeException("Parameter for run [Location] was not set to 'Local' or 'Remote'");
		}

		driver.manage().timeouts().setScriptTimeout(Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT, TimeUnit.SECONDS).implicitlyWait(Constants.ELEMENT_TIMEOUT, TimeUnit.SECONDS);	
		setDefaultTestTimeout(Constants.DEFAULT_GLOBAL_DRIVER_TIMEOUT);
		//driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		setDriverWindow(driver.getWindowHandle());

	}
}
