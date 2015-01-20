package com.orasi.api.SoapExamples.predict8.IDServiceBinding;

import com.orasi.api.soapServices.core.*;

public class IDServiceBinding extends SoapService{
	public IDServiceBinding(String environment) {	
		setEnvironmentServiceURL("http://www.predic8.com:8080/base/IDService");	
	}
}
