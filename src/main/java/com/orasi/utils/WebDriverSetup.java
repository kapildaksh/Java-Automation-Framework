
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
	private String testName = "";
	private String testEnvironment = "";
	private String testApplication = "";
	private String driverWindow = "";
	private String application = "";
	public Datatable scenario = new Datatable();
	public ResourceBundle appURLRepository = ResourceBundle.getBundle(Constants.ENVIRONMENT_URL_PATH);
	public String browser = null;
	public String location = null;
	private URL seleniumHubURL = null;
	private static int TIMEOUT = 20;
	
	public WebDriverSetup(){}
	
	public WebDriverSetup(String testName){
		this.testName = testName;
	}
	
	public WebDriverSetup(String testName, String application, String browserUnderTest, String runLocation, String environment){
		boolean isGrid = false;
		this.application = application;
		this.testEnvironment = environment;
		this.browser = browserUnderTest;
		this.location = runLocation;
		this.testName = testName;
		if (runLocation.toLowerCase().equals("remote")) isGrid = true;
	
     
	}
	
	
	public WebDriver getDriver(){
		return driver;
	}
	
	public WebDriver initialize(){
		
		driverSetup();
		launchApplication(driver);
		return this.driver;
	}
	
	/*
	public WebDriver initialize(String application, String browserUnderTest, String runLocation, String environment){
		boolean isGrid = false;
		this.application = application;
		this.testEnvironment = environment;
		if (runLocation.toLowerCase().equals("remote")) isGrid = true;		
		new FrameworkHarness().hookToWDPro(application, environment, 20, browserUnderTest, isGrid);
		SeleniumWrapper seleniumWrapper = new SeleniumWrapper();
		
		//Set the driver type based on the browserUnderTest
		switch(browserUnderTest.toLowerCase()){
			case "ie":
			case "iexplorer":
				seleniumWrapper.getConfiguration().setDriverType(DriverType.INTERNET_EXPLORER); 
				break;
			case "firefox":
				seleniumWrapper.getConfiguration().setDriverType(DriverType.FIREFOX);
				break;
			case "chrome":
				seleniumWrapper.getConfiguration().setDriverType(DriverType.CHROME);
				break;
			case "htmlunit":
				seleniumWrapper.getConfiguration().setDriverType(DriverType.HTML_UNIT);
				break;
			
		}
		  
		 
       try {
			this.driver = seleniumWrapper.initialize(testName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
       
       return this.driver;
	}*/
	
	public void launchApplication(WebDriver driver){
		driver.get(appURLRepository.getString(application.toUpperCase() + "_" + testEnvironment.toUpperCase()));
		
	}
	
	public static String getBrowserUnderTest(){
		return System.getProperty("selenium.rc_type");
	}
	
	public static String getApplicationUnderTest(){
		return System.getProperty("selenium.application");
	}
	
	public static String getEnvironmentUnderTest(){
		return System.getProperty("selenium.environment");
	}
	
	public static int getDefaultTestTimeout(){
		return Integer.parseInt(System.getProperty("selenium.default_timeout"));
	}
	
	//@BeforeMethod
	@Parameters({"runLocation","browserUnderTest"})
	public void driverSetup(){
		//browser = browserUnderTest;
		//location = runLocation;
		try {
			seleniumHubURL = new URL(Constants.SELENIUM_HUB_URL);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Selenium Hub URL set is not a valid URL: " + seleniumHubURL);
		}

		driver = null;

		if (location.equalsIgnoreCase("local")){
			if (browser.equalsIgnoreCase("Firefox")){
		    	driver = new FirefoxDriver();	    	
		    }
		    else if(browser.equalsIgnoreCase("IE")){
		    	DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
		    	caps.setCapability("ignoreZoomSetting", true);
		    	caps.setCapability("enablePersistentHover", false);
		    	// Setting attribute nativeEvents to false enable click button in IE
		    	//caps.setCapability("nativeEvents",true);	    	

		    	File file = new File("C:\\Selenium\\WebDrivers\\IEDriverServer.exe");
				System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
				//InternetExplorerDriverService service = new InternetExplorerDriverService.Builder().withLogFile(new File("path-to-file")).withLogLevel(InternetExplorerDriverLogLevel.TRACE).build();
				driver = new InternetExplorerDriver(caps);
		    }
		    else if(browser.equalsIgnoreCase("Chrome")){
		    	File file = new File("C:\\Selenium\\WebDrivers\\ChromeDriver.exe");
				System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
				driver = new ChromeDriver();		    	
		    }
		    else if(browser.equalsIgnoreCase("html")){	    	
				driver = new HtmlUnitDriver(true);		    	
		    }
		}else if(location.equalsIgnoreCase("remote")){
			if (browser.equalsIgnoreCase("Firefox")){
				DesiredCapabilities caps = DesiredCapabilities.firefox();
				caps.setPlatform(org.openqa.selenium.Platform.WINDOWS);
		    	driver = new RemoteWebDriver(seleniumHubURL, caps);	    	
		    }
		    else if(browser.equalsIgnoreCase("IE")){
		    	DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
		    	caps.setCapability("ignoreZoomSetting", true);
		    	caps.setPlatform(org.openqa.selenium.Platform.WINDOWS);		    	
				driver = new RemoteWebDriver(seleniumHubURL, caps);	
		    }
		    else if(browser.equalsIgnoreCase("Chrome")){
		    	DesiredCapabilities caps = DesiredCapabilities.chrome();
				caps.setPlatform(org.openqa.selenium.Platform.WINDOWS);
		    	driver = new RemoteWebDriver(seleniumHubURL, caps);	    		    	
		    }
		    else if(browser.equalsIgnoreCase("html")){	    	
				driver = new HtmlUnitDriver(true);		    	
		    }
		}else{
			throw new RuntimeException("Parameter for run [Location] was not set to 'Local' or 'Remote'");
		}

		driver.manage().timeouts().setScriptTimeout(Constants.GLOBAL_DRIVER_TIMEOUT, TimeUnit.SECONDS).implicitlyWait(Constants.GLOBAL_DRIVER_TIMEOUT, TimeUnit.SECONDS);	
		//driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		setDriverWindow(driver.getWindowHandle());

	}

	@Parameters({"environment"})	
	//@BeforeTest
	public void setEnvironment(String environment){		
		//Utility.generateCreditCardData("Visa", "Approved", "0");
		setCurrentEnvironment(environment);	
	}


	private void setCurrentEnvironment(String environment){
		testEnvironment = environment;
	}
	
	public  String getCurrentEnvironment(){

		return testEnvironment;
	}

	public  void setCurrentApplication(String application){
		testApplication= application;
	}

	public String getCurrentApplication(){
		return testApplication;
	}


	public void setDriverWindow(String window){
		driverWindow= window;
	}

	public String getDriverWindow(){
		return driverWindow;
	}

	
	public  ResourceBundle getEnvironmentURLRepository(){
		return appURLRepository;
	}

	public static void setDefaultTestTimeout(int timeout){
		TIMEOUT = timeout;
	}
	
	/*public static int getDefaultTestTimeout(){
		return TIMEOUT;
	}*/
	
	public void setDriver(WebDriver driverSession){
		driver = driverSession;
	}
}
