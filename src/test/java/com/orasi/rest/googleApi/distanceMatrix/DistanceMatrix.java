package com.orasi.rest.googleApi.distanceMatrix;

import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.orasi.api.restServices.core.RestService;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class DistanceMatrix {
	String application;
	String runLocation;
	String operatingSystem;
	String environment;
	String[] validateOriginsArray;
	int numberOfOrigins;
	String[] validateDestinationsArray;
	int numberOfDestinations;
	RestService rest = null;

	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		return new ExcelDataProvider(Constants.GOOGLEAPI_DATAPROVIDER_PATH
				+ "DistanceMatrix.xlsx", "DistanceMatrix").getTestData();
	}

	@BeforeTest(groups = { "rest" })
	@Parameters({ "environment", "runLocation", "operatingSystem" })
	public void setup(String runLocation, String operatingSystem,
			String environment) {
		this.application = "GoogleAPI";
		this.runLocation = runLocation;
		this.operatingSystem = operatingSystem;
		this.environment = environment;
	}

    /**
     * @throws XPathExpressionException
     * @throws JSONException 
     * @Summary: Invokes the Google "Geocode" API REST service and validates the XML response
     * @Precondition:NA
     * @Author: Jessica Marshall
     * @Version: 10/6/2014
     * @Return: N/A
     */
	@Test(dataProvider = "dataScenario", groups = { "rest" })
	public void main(String testScenario, String origins, String destinations,
						String mode, String language, String apiKey,
						String validateOriginAddress, String validateDestinationAddress,
						String format) throws XPathExpressionException, JSONException {
		rest = new RestService();
		rest.setDefaultResponseFormat(format);
		rest.sendGetRequest(
				"https://maps.googleapis.com/maps/api/distancematrix/"+rest.getDefaultResponseFormat()+"?origins="+origins+"&destinations="+destinations+"&mode="+mode+"&language="+language+"&key="+apiKey,
				rest.getDefaultResponseFormat());

		if(rest.getDefaultResponseFormat().equalsIgnoreCase("xml")){
			String xpath = "/DistanceMatrixResponse";
			int numberOfNodes = rest.getNumberOfNodesByXpath(xpath);
			int numberOfChildNodes = rest.getNumberOfChildNodesByXpath(xpath);
			System.out.println("Number of nodes for xpath["+xpath+"]: " +String.valueOf(numberOfNodes));
			System.out.println("Number of childs nodes fro xpath ["+xpath+"]: "+String.valueOf(numberOfChildNodes));
			
			Assert.assertEquals(rest.getXmlResponseByXpath("/DistanceMatrixResponse/origin_address[1]"), "Vancouver, BC, Canada");
			Assert.assertEquals(rest.getXmlResponseByXpath("/DistanceMatrixResponse/destination_address[2]"), "Victoria, BC, Canada");
			
		}else{
			//TODO: Needs JSON validations here
		}
	}
}
