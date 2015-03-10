package com.orasi.api.SoapExamples.predict8.CRMServicePTBinding.operations;

import javax.xml.bind.ValidationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.testng.Assert;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.orasi.utils.XMLTools;
import com.orasi.api.SoapExamples.predict8.CRMServicePTBinding.CRMServicePTBinding;

public class GetAll extends CRMServicePTBinding{
	public GetAll(String environment, String scenario) {
		super(environment);
		
		//Generate a request from a project xml file
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("getAll")));
		System.out.println(getRequest());
				
		removeComments() ;
		removeWhiteSpace();
	}
	
	public void verifyPresenceOfCustomer(String[] attributes, String path, boolean isExpected)
			throws XPathExpressionException, ValidationException {
		boolean articleFound = false;
		String nodeText;
		
		// creating an XPathFactory:
		XPathFactory factory = XPathFactory.newInstance();
		// using this factory to create an XPath object:
		XPath xpath = factory.newXPath();

		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(path);
		Object result = expr.evaluate(getResponseDocument(), XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		
		for(int nodeCounter = 0; nodeCounter < nodes.getLength(); nodeCounter++){
			Node node = nodes.item(nodeCounter);
			nodeText = node.getTextContent();
			if(nodeText.contains(attributes[attributes.length-1])){
				articleFound = true;
				for(int attributeCounter = 0; attributeCounter < attributes.length; attributeCounter++){
					if(!nodeText.contains(attributes[attributeCounter])){
						throw new ValidationException("The attribute ["+attributes[attributeCounter]+"] was not found in the node identified to have article id ["+nodeText.contains(attributes[4])+"]. The node contents are as follows: ["+nodeText+"].");
					}
				}
				break;
			}
		}
		if(isExpected != articleFound){
			String errorMessage;
			if(isExpected){
				errorMessage = "The article with id ["+attributes[4]+"] was not found, but was expected";
			}else{
				errorMessage = "The article with id ["+attributes[4]+"] was found, but was not expected";
			}
			Assert.assertEquals(articleFound, isExpected, errorMessage);
		}
	}
}
