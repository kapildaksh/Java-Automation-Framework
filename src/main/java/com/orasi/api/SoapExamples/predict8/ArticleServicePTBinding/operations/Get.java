package com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations;

import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.ArticleServicePTBinding;
import com.orasi.utils.XMLTools;

public class Get extends ArticleServicePTBinding{
	public Get(String environment, String scenario) {
		super(environment);
		
		//Generate a request from a project xml file
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("get")));
		System.out.println(getRequest());
				
		removeComments() ;
		removeWhiteSpace();
	}

}
