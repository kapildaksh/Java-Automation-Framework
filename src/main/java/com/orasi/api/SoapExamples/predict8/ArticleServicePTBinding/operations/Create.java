package com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations;

import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.ArticleServicePTBinding;
import com.orasi.utils.XMLTools;

public class Create extends ArticleServicePTBinding{
	public Create(String environment, String scenario) {
		super(environment);
		
		//Generate a request from a project xml file
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("create")));
		System.out.println(getRequest());
				
		removeComments() ;
		removeWhiteSpace();
	}

}
