package com.orasi.sandbox;

import javax.xml.bind.ValidationException;
import javax.xml.xpath.XPathExpressionException;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.orasi.api.SoapExamples.predict8.CRMServicePTBinding.operations.*;


public class WaitsSandbox {
	String personId;
	String personFirstName;
	String personLastName;
	String personAddressStreet;
	String personAddressCity;
	String personAddressZipCode;
	String personAddressCountry;
	String personAge;
	String addressStreet;
	String addressCity;
	String addressZipCode;
	String addressCountry;
	String addressCompanyName;
	String id;
	String[] attributes;
	
	@Test()
	public void main() throws XPathExpressionException, ValidationException{
		Create create = new Create("stage","Main");
		attributes = setCustomerValues(create);
		create.sendRequest();
		Assert.assertEquals(create.getResponseStatusCode(), "200");
		
		Get get = new Get("stage","Main");
		get.setRequestId(id);
		get.sendRequest();
		Assert.assertEquals(get.getResponseStatusCode(), "200");
		validateResponseValues(get);
		
		GetAll getAll = new GetAll("stage","Main");
		getAll.sendRequest();
		Assert.assertEquals(getAll.getResponseStatusCode(), "200");
		getAll.verifyPresenceOfCustomer(attributes, "/Envelope/Body/getAllResponse/customer", true);
	}
	
	private String[] setCustomerValues(Create localCreate){
		System.out.println();
		personId = localCreate.getPersonId();
		System.out.println("Person ID: " + personId);
		personFirstName = localCreate.getPersonFirstName();
		System.out.println("Person First Name: " + personFirstName);
		personLastName = localCreate.getPersonLastName();
		System.out.println("Person Last Name: " + personLastName);
		personAddressStreet = localCreate.getPersonAddressStreet();
		System.out.println("Person Address Street: " + personAddressStreet);
		personAddressCity = localCreate.getPersonAddressCity();
		System.out.println("Person Address City: " + personAddressCity);
		personAddressZipCode = localCreate.getPersonAddressZipCode();
		System.out.println("Person Address Zip Code: " + personAddressZipCode);
		personAddressCountry = localCreate.getPersonAddressCountry();
		System.out.println("Person Address Country: " + personAddressCountry);
		personAge = localCreate.getPersonAge();
		System.out.println("Person Age: " + personAge);
		addressStreet = localCreate.getAddressStreet();
		System.out.println("Address Street: " + addressStreet);
		addressCity = localCreate.getAddressCity();
		System.out.println("Address City: " + addressCity);
		addressZipCode = localCreate.getAddressZipCode();
		System.out.println("Address Zip Code: " + addressZipCode);
		addressCountry = localCreate.getAddressCountry();
		System.out.println("Address Country: " + addressCountry);
		addressCompanyName = localCreate.getAddressCompanyName();
		System.out.println("Address Company Name: " + addressCompanyName);
		id = localCreate.getId();
		System.out.println("ID: " + id);
		System.out.println();
		String[] attributes = {personId, personFirstName, personLastName, personAddressStreet, personAddressCity,
				personAddressZipCode, personAddressCountry, personAge, addressStreet, addressCity,
				addressZipCode, addressCountry, addressCompanyName, id};
		return attributes;
	}
	
	private void validateResponseValues(Get localGet){
		Assert.assertEquals(localGet.getResponsePersonId(), personId, "The response person ID ["+localGet.getResponsePersonId()+"] was not that which was expected ["+personId+"].");
		Assert.assertEquals(localGet.getResponsePersonFirstName(), personFirstName, "The response person first name ["+localGet.getResponsePersonFirstName()+"] was not that which was expected ["+personFirstName+"].");
		Assert.assertEquals(localGet.getResponsePersonLastName(), personLastName, "The response person last name ["+localGet.getResponsePersonLastName()+"] was not that which was expected ["+personLastName+"].");
		Assert.assertEquals(localGet.getResponsePersonAddressStreet(), personAddressStreet, "The response person address street ["+localGet.getResponsePersonAddressStreet()+"] was not that which was expected ["+personAddressStreet+"].");
		Assert.assertEquals(localGet.getResponsePersonAddressCity(), personAddressCity, "The response person address city ["+localGet.getResponsePersonAddressCity()+"] was not that which was expected ["+personAddressCity+"].");
		Assert.assertEquals(localGet.getResponsePersonAddressZipCode(), personAddressZipCode, "The response person address zip code ["+localGet.getResponsePersonAddressZipCode()+"] was not that which was expected ["+personAddressZipCode+"].");
		Assert.assertEquals(localGet.getResponsePersonAddressCountry(), personAddressCountry, "The response person address country ["+localGet.getResponsePersonAddressCountry()+"] was not that which was expected ["+personAddressCountry+"].");
		Assert.assertEquals(localGet.getResponsePersonAge(), personAge, "The response person age ["+localGet.getResponsePersonAge()+"] was not that which was expected ["+personAge+"].");
		Assert.assertEquals(localGet.getResponseAddressStreet(), addressStreet, "The response address street ["+localGet.getResponseAddressStreet()+"] was not that which was expected ["+addressStreet+"].");
		Assert.assertEquals(localGet.getResponseAddressCity(), addressCity, "The response address street ["+localGet.getResponseAddressCity()+"] was not that which was expected ["+addressCity+"].");
		Assert.assertEquals(localGet.getResponseAddressZipCode(), addressZipCode, "The response address street ["+localGet.getResponseAddressZipCode()+"] was not that which was expected ["+addressZipCode+"].");
		Assert.assertEquals(localGet.getResponseAddressCountry(), addressCountry, "The response address street ["+localGet.getResponseAddressCountry()+"] was not that which was expected ["+addressCountry+"].");
		Assert.assertEquals(localGet.getResponseAddressCompanyName(), addressCompanyName, "The response address street ["+localGet.getResponseAddressCompanyName()+"] was not that which was expected ["+addressCompanyName+"].");
	}
}
