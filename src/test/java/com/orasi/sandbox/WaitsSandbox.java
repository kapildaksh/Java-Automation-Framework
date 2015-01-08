package com.orasi.sandbox;

import javax.xml.soap.MimeHeaders;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations.GetAll;
import com.orasi.api.soapServices.core.SoapService;

public class WaitsSandbox {
	SoapService soap = null;	
	
	@Test()
	public void main(){		
		GetAll all = new GetAll("stage","Main");
		all.sendRequest();
	}
}
