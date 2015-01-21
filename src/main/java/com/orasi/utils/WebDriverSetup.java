package com.orasi.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.NotConnectedException;
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
		setBrowserUnderTest(browserUnderTest);
		this.browserVersion = browserVersion;
		setBrowserVersion(browserVersion);
		this.operatingSystem = operatingSystem;
		setOperatingSystem(operatingSystem);
		this.location = runLocation;
		setRunLocation(runLocation);
		this.testEnvironment = environment;
		
		verifyExpectedAndActualOS();
	}
	
	//Getters & Setters
	public void setTestEnvironment(String environment){
		testEnvironment = environment;
	}
	
	public  String getTestEnvironment(){
		return testEnvironment;
	}

	public static void setTestApplication(String application){
		System.setProperty("testApplication",application);
	}

	public static String getTestApplication(){
		return System.getProperty("testApplication");
	}

	public void setDriverWindow(String window){
		driverWindow= window;
	}

	public String getDriverWindow(){
		return driverWindow;
	}

	public static String getOperatingSystem() {
		return System.getProperty("operatingSystem");
	}

	public static void setOperatingSystem(String operatingSystem) {
		System.setProperty("operatingSystem", operatingSystem);
	}

	public static void setBrowserUnderTest(String browser) {
		System.setProperty("browser", browser);
	}
	
	public static String getBrowserUnderTest(){
		return System.getProperty("browser");
	}
	
	public static String getBrowserVersion() {
		return System.getProperty("browserVersion");
	}

	public static void setBrowserVersion(String browserVersion) {
		System.setProperty("browserVersion", browserVersion);
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
	
	public static String getRunLocation() {
		return System.getProperty("runLocation");
	}

	public static void setRunLocation(String location) {
		System.setProperty("runLocation", location);
	}
	
	/**
	 * Initializes the webdriver, sets up the run location, driver type,
	 * launches the application.
	 * 
	 * @param	None
	 * @version	12/16/2014
	 * @author 	Jessica Marshall
	 * @return 	the web driver
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public WebDriver initialize() throws InterruptedException, IOException{
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
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void driverSetup() throws InterruptedException, IOException, NotConnectedException{
		//Set the URL for selenium grid
		try {
			seleniumHubURL = new URL(Constants.SELENIUM_HUB_URL);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Selenium Hub URL set is not a valid URL: " + seleniumHubURL);
		}

		driver = null;

		//If the location is local, grab the drivers for each browser type from within the project
		if (getRunLocation().equalsIgnoreCase("local")){
			DesiredCapabilities caps = null;
			File file = null;
			switch (getOperatingSystem().toLowerCase().trim().replace(" ", "")) {
			case "windows": case "": case "vista":
				if (getBrowserUnderTest().equalsIgnoreCase("Firefox") || getBrowserUnderTest().equalsIgnoreCase("FF")){
			    	driver = new FirefoxDriver();	    	
			    }
				//Internet explorer
			    else if(getBrowserUnderTest().equalsIgnoreCase("IE") || getBrowserUnderTest().replace(" ", "").equalsIgnoreCase("internetexplorer")){
			    	caps = DesiredCapabilities.internetExplorer();
			    	caps.setCapability("ignoreZoomSetting", true);
			    	caps.setCapability("enablePersistentHover", false);
			    	file = new File(this.getClass().getResource(Constants.DRIVERS_PATH_LOCAL + "IEDriverServer.exe").getPath());
					System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
					driver = new InternetExplorerDriver(caps);
			    }
				//Chrome
			    else if(getBrowserUnderTest().equalsIgnoreCase("Chrome")){
			    	file = new File(this.getClass().getResource(Constants.DRIVERS_PATH_LOCAL + "ChromeDriver.exe").getPath());
					System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
					driver = new ChromeDriver();		    	
			    }
				//Headless - HTML unit driver
			    else if(getBrowserUnderTest().equalsIgnoreCase("html")){	    	
					driver = new HtmlUnitDriver(true);		    	
			    }
				//Safari
			    else if(getBrowserUnderTest().equalsIgnoreCase("safari")){
			    	driver = new SafariDriver();
			    }
			    else {
			    	throw new RuntimeException("Parameter not set for browser type");
			    }
				break;
			case "mac":case "macos":
				if (getBrowserUnderTest().equalsIgnoreCase("Firefox") || getBrowserUnderTest().equalsIgnoreCase("FF")){
			    	driver = new FirefoxDriver();	    	
			    }
				//Internet explorer
			    else if(getBrowserUnderTest().equalsIgnoreCase("IE") || getBrowserUnderTest().replace(" ", "").equalsIgnoreCase("internetexplorer")){
			    	throw new RuntimeException("Currently there is no support for IE with Mac OS.");
			    }
				//Chrome
			    else if(getBrowserUnderTest().equalsIgnoreCase("Chrome")){
			    	file = new File(this.getClass().getResource(Constants.DRIVERS_PATH_LOCAL + "mac/chromedriver").getPath());
			    	System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
					try{
						//Ensure the permission on the driver include executable permissions
						Process proc = Runtime.getRuntime().exec(new String[]{"/bin/bash","-c","chmod 777 " + file.getAbsolutePath()});
						proc.waitFor();									
						driver = new ChromeDriver();
					}catch(IllegalStateException ise){
						ise.printStackTrace();
						throw new IllegalStateException("This has been seen to occur when the chromedriver file does not have executable permissions. In a terminal, navigate to the directory to which Maven pulls the drivers at runtime (e.g \"/target/classes/drivers/\") and execute the following command: chmod +rx chromedriver");
					}catch(IOException ioe){
						ioe.printStackTrace();
					}catch(InterruptedException ie){
						ie.printStackTrace();
					}
			    }
				//Headless - HTML unit driver
			    else if(getBrowserUnderTest().equalsIgnoreCase("html")){	    	
					driver = new HtmlUnitDriver(true);		    	
			    }
				//Safari
			    else if(getBrowserUnderTest().equalsIgnoreCase("safari")){
			    	driver = new SafariDriver();
			    }
			    else {
			    	throw new RuntimeException("Parameter not set for browser type");
			    }
				break;
			case "linux":case "linuxos":
				if (getBrowserUnderTest().equalsIgnoreCase("Firefox") || getBrowserUnderTest().equalsIgnoreCase("FF")){
					driver = new FirefoxDriver();
			    }else if(getBrowserUnderTest().equalsIgnoreCase("Chrome")){
			    	file = new File(this.getClass().getResource(Constants.DRIVERS_PATH_LOCAL + "/linux/chromedriver").getPath());
			    	System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
					try{
						//Ensure the permission on the driver include executable permissions
						Process proc = Runtime.getRuntime().exec(new String[]{"/bin/bash","-c","chmod 777 " + file.getAbsolutePath()});
						proc.waitFor();									
						driver = new ChromeDriver();
					}catch(IllegalStateException ise){
						ise.printStackTrace();
						throw new IllegalStateException("This has been seen to occur when the chromedriver file does not have executable permissions. In a terminal, navigate to the directory to which Maven pulls the drivers at runtime (e.g \"/target/classes/drivers/\") and execute the following command: chmod +rx chromedriver");
					}catch(IOException ioe){
						ioe.printStackTrace();
					}catch(InterruptedException ie){
						ie.printStackTrace();
					}
			    }
				break;
			default:
				break;
			}			
		
		//Code for running on the selenium grid
		}else if(getRunLocation().equalsIgnoreCase("remote")){
			
			DesiredCapabilities caps = null;
			
			//firefox
			if (getBrowserUnderTest().equalsIgnoreCase("Firefox")){
				caps = DesiredCapabilities.firefox();
				caps.setVersion(browserVersion);    	
		    }
			//internet explorer
		    else if(getBrowserUnderTest().equalsIgnoreCase("IE")){
		    	caps = DesiredCapabilities.internetExplorer();
		    	caps.setCapability("ignoreZoomSetting", true);
		    	caps.setVersion(browserVersion);
		    }
			//chrome
		    else if(getBrowserUnderTest().equalsIgnoreCase("Chrome")){
		    	caps = DesiredCapabilities.chrome();
		    	caps.setVersion(browserVersion);  		    	
		    }
			//headless - HTML unit driver
		    else if(getBrowserUnderTest().equalsIgnoreCase("html")){	
		    	caps = DesiredCapabilities.htmlUnitWithJs();		    	
		    }
			//safari
		    else if(getBrowserUnderTest().equals("safari")){
		    	caps = DesiredCapabilities.safari();
		    }
		    else {
		    	throw new RuntimeException("Parameter not set for browser type");
		    }
			
			caps.setPlatform(org.openqa.selenium.Platform.valueOf(getOperatingSystem()));
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
	
	/**
	 * Verifies that the OS passed by the TestNG XML is the same as the actual OS 
	 * @version	01/16/2015
	 * @author 	Waightstill W Avery
	 */
	private void verifyExpectedAndActualOS(){
		//Verify that the current OS is actually that which was indicated as expected by the TestNG XML
		String platform = Platform.getCurrent().toString().toLowerCase();
		switch (operatingSystem) {
		/*
		 * Mac OS, Linux, Unix and Android are OS enumerations that have only one value. 
		 * Windows is treated as the default case, but a validation is made that the 
		 * current Windows OS is one that can be handled by the framework.
		 */
		case "mac": case "linux": case "unix": case "android":
			TestReporter.assertTrue(platform.trim().replace(" ", "").equalsIgnoreCase(operatingSystem.toString().toLowerCase().trim().replace(" ", "")), "The System OS ["+platform.trim().replace(" ", "")+"] did not match that which was passed in the TestNG XML ["+operatingSystem.toString().toLowerCase().trim().replace(" ", "")+"].");
			break;			
		default:
			String[] knownPlatformValues = {"windows", "xp", "vista", "win8", "win8_1"};
			Boolean osFound = false;
			for(int winCount = 0; winCount < knownPlatformValues.length; winCount++){
				osFound = platform.equalsIgnoreCase(knownPlatformValues[winCount]);
				if(osFound){
					break;
				}
			}
			TestReporter.assertTrue(osFound, "The System OS ["+platform+"] did not match that which was passed in the TestNG XML ["+operatingSystem+"].");
			break;
		}
	}
}