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

public class DistanceMatrix_JSON {
	String application;
	String runLocation;
	String operatingSystem;
	String environment;
	String streetNumber;
	String streetNumberTarget;
	String streetname;
	String streetNameTarget;
	String postalCode;
	String postalCodeTarget;
	String city;
	String cityTarget;
	String fullAddress;
	RestService rest = null;

	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		return new ExcelDataProvider(Constants.GOOGLEAPI_DATAPROVIDER_PATH
				+ "Geocode.xlsx", "Geocode").getTestData();
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
     * @Summary: Invokes the Google "Geocode" API REST service and validates the JSON response
     * @Precondition:NA
     * @Author: Jessica Marshall
     * @Version: 10/6/2014
     * @Return: N/A
     */
	@Test(dataProvider = "dataScenario", groups = { "rest" })
	public void main(String testScenario, String streetNumber, String streetNumberTagert,
			String streetname, String streetNameTarget,
			String postalCode, String postalCodeTarget,
			String city, String cityTarget, String fullAddress) throws XPathExpressionException, JSONException {
		rest = new RestService();
		rest.setDefaultResponseFormat("json");
		rest.sendGetRequest(
				"https://maps.googleapis.com/maps/api/geocode/"+rest.getDefaultResponseFormat()+"?sensor=false&address=7025 Albert Pick Rd Greensboro NC, 27409",
				rest.getDefaultResponseFormat());
	}

	private void validateAddressValue(String tagetValue, String value,
			int maxIndex) throws XPathExpressionException {
		String path = "";
		int numberOfNodes = 0;
		boolean nodeFound = false;

		for (int loopCounter = 1; loopCounter <= maxIndex; loopCounter++) {
			path = "/GeocodeResponse/result/address_component["
					+ String.valueOf(loopCounter) + "]/type";
			numberOfNodes = rest.getNumberOfNodesByXpath(path);
			if (numberOfNodes > 1) {
				for (int nodeCounter = 1; nodeCounter <= numberOfNodes; nodeCounter++) {
					path = "/GeocodeResponse/result/address_component["
							+ String.valueOf(loopCounter) + "]/type["
							+ String.valueOf(nodeCounter) + "]";
					if (rest.getXmlResponseByXpath(path).equalsIgnoreCase(
							tagetValue)) {
						path = path.replace(
								"/type[" + String.valueOf(nodeCounter) + "]",
								"/long_name");
						Assert.assertEquals(
								rest.getXmlResponseByXpath(path),
								value,
								"For the target value of [" + tagetValue
										+ "] the value of [" + value
										+ "] was expected, but ["
										+ rest.getXmlResponseByXpath(path)
										+ "] was found");
						nodeFound = true;
						break;
					}
				}
				if (nodeFound) {
					break;
				}
			} else {
				if (rest.getXmlResponseByXpath(path).equalsIgnoreCase(
						tagetValue)) {
					path = path.replace("/type", "/long_name");
					Assert.assertEquals(rest.getXmlResponseByXpath(path), value);
					break;
				}
			}
		}
	}
	
/*	private void readInDataValues(){
		streetNumber = Datatable.;
		streetNumberTarget;
		streetname;
		streetNameTarget;
		postalCode;
		postalCodeTarget;
		city;
		cityTarget;
		fullAddress;
	}*/
}
