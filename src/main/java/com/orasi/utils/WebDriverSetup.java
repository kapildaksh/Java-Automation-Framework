package com.orasi.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
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
import org.testng.Assert;

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
	
	//Define a variable to house the Linux OS username
	String username = "";
		
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
					linuxFirefoxSetup();
					
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
			TestReporter.assertTrue(osFound, "Validating expected vs. actual operating systems");
			Assert.assertTrue(osFound, "The System OS ["+platform+"] did not match that which was passed in the TestNG XML ["+operatingSystem+"].");
			break;
		}
	}
	
	/**
	 *  Verifies that a suitable version of Firefox is installed on the Linux 
	 *  box and that a soft/symbolc link is available for Selenium to access
	 *  @version 01/22/2015
	 *  @author Waightstill W Avery
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public void linuxFirefoxSetup() throws IOException, InterruptedException{
		/*
		 * The expected path is /home/<username>/firefox/firefox
		 * If this file is not found, the binaries will be downloaded 
		 *   from Mozilla to the current working directory, unzip and 
		 *   extracted and mover to the aforementioned location.
		 * Whether the binaries are found or need to be downloaded, the
		 *   last step will be to make an alias for the executable with 
		 *   which Selenium will use to launch Firefox. 
		 */
		//Define the expected home directory location
		String homeDirectory = "/home";	
		//Define the Firefox version
		String firefoxVersion = browserVersion;
		Assert.assertEquals(browserVersion.isEmpty(), false, "To ensure Firefox binaries are loaded on a Linux OS, a browser version is needed (e.g. 31.0) to be passed from the testNG XML.");
		//Define the binary directory
		String binaryDirectory = "firefox";
		//Define the expected binary location from the user's home directory
		String binaryLocation = binaryDirectory+"/firefox";
		//Define the tarball name containing the binaries
		String binaryContainer = "firefox-"+firefoxVersion+".tar.bz2";
		//Define the binary tarball
		String binaryTarball = "firefox-"+firefoxVersion+".tar";
		//String binary mozilla URL
		String binaryMozillaUrl = "https://ftp.mozilla.org/pub/mozilla.org/firefox/releases/"+firefoxVersion+"/linux-x86_64/en-US/firefox-"+firefoxVersion+".tar.bz2";
		//Define the 'bash' executable location and associated arguments
		String bashLocation = "/bin/bash";
		String bashArguments = "-c";
		//Create a Java Process object that will be used throughout this flow
		Process proc = null;
		//Creare a Java File object that will be used throughout this flow
		File file = null;
		
		if(!linuxFirefoxfindBinaries(homeDirectory, binaryLocation)){			
			//Use 'wget' to download the required files
			System.out.print("Downloading file from: "+binaryMozillaUrl);
			proc = Runtime.getRuntime().exec(new String[]{bashLocation,bashArguments,"wget " + binaryMozillaUrl });
			proc.waitFor();
			System.out.println(".......done");
			Assert.assertEquals(checkShellProcessForErrors(proc), 0, "Invoking wget on the URL ["+binaryMozillaUrl+"] resulted in an error. The Linux Firefox binaries were not downloaded successfully.");
			
			//Unzip the file
			System.out.print("Unzipping the file: "+binaryContainer);
			proc = Runtime.getRuntime().exec(new String[]{bashLocation,bashArguments,"bunzip2 " + binaryContainer });
			proc.waitFor();
			System.out.println(".......done");
			Assert.assertEquals(checkShellProcessForErrors(proc), 0, "An error was encountered while unzipping the file ["+binaryContainer+"].");
			
			//Extract the file files from the archive
			System.out.print("Extracting the files from: "+binaryTarball);
			proc = Runtime.getRuntime().exec(new String[]{bashLocation,bashArguments,"tar -xvf " + binaryTarball});
			proc.waitFor();
			System.out.println(".......done");
			Assert.assertEquals(checkShellProcessForErrors(proc), 0, "An error was encountered while decompressing the tarball ["+binaryTarball+"].");
			
			//Move the file to the preferred location
			System.out.print("Moving the directory: "+binaryDirectory);
			proc = Runtime.getRuntime().exec(new String[]{bashLocation,bashArguments,"mv firefox " + homeDirectory + "/"+ username});
			proc.waitFor();
			System.out.println(".......done");
			Assert.assertEquals(checkShellProcessForErrors(proc), 0, "An error was encountered moving the file from the current working directory to ["+homeDirectory + "/"+ username+"].");
			
			//Verify that the binaries are now found
			System.out.print("Verifying binaries");
			Assert.assertTrue(linuxFirefoxfindBinaries(homeDirectory, binaryLocation), "The binaries were not found after extracting the files from the tarball.");
			file = new File(homeDirectory + "/" + username + "/" + binaryLocation);
			Assert.assertTrue(file.canExecute(), "The file was found, but is not an executable.");
			System.out.println(".......done");
		}
	}
	
	/**
	 *  Attempts to locate Firefox binaries
	 *  @version 01/22/2015
	 *  @author Waightstill W Avery
	 *  @param homeDirectory - absolute path of the home directory; typically '/home'
	 *  @param binaryLocation - relative path of the binaries from the home directory
	 *  @return boolean - indicates whether the binaries were located
	 */
	private boolean linuxFirefoxfindBinaries(String homeDirectory, String binaryLocation){
		boolean binariesFound = false;
		File file = new File(homeDirectory);
		//Ensure the home directory exists
		Assert.assertTrue(file.exists(), "The expected home directory '"+homeDirectory+"' was not found to exist.");
		Assert.assertTrue(file.isDirectory(), "An object with the path '"+homeDirectory+"' was found to exist but is not a directory.");
		/*
		 * Grab a list of the contents of the home directory and iterate 
		 *   through each item to see if the binarie exists
		 */
		String[] contents = file.list();
		Assert.assertNotEquals(contents.length, 0, "The home directory was found, but did not contain any child items or content.");
		for(String content: contents){
			username = content;
			file = new File(homeDirectory + "/" + username);
			if(file.isDirectory()){
				file = new File(homeDirectory + "/" + content + "/" + binaryLocation);
				if(file.exists()){
					binariesFound = true;
					break;
				}
			}
		}
		return binariesFound;
	}
	
	/**
	 *  Checks a given Java Process object for errors
	 *  @version 01/22/2015
	 *  @author Waightstill W Avery
	 *  @param proc - Java Process object
	 *  @return integer - typically 0 for success, otherwise indicates a 
	 *  	failure and possibly relate to a particular error code
	 * 	@throws IOException 
	 */
	private int checkShellProcessForErrors(Process proc) throws IOException{
		if(proc.exitValue() != 0){
			String s = null;
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			while ((s = stdError.readLine()) != null) {
		        System.out.println(s);
		    }
		}
		return proc.exitValue();
	}
}