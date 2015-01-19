package com.orasi.soap.predic8.idServiceBinding;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.orasi.api.SoapExamples.predict8.IDServiceBinding.operations.Generate;

public class GenerateTest {
	@Test()
	public void main() {
		Generate gen = new Generate("stage", "Main");
		gen.updateGenerateValue("1");
		gen.sendRequest();
		String input = gen.getGenerateValue();
		Boolean isFound = gen.getResponseId().split("-")[0].equalsIgnoreCase(input);
		Assert.assertEquals(isFound, Boolean.TRUE, "The generate value ["+input+"] was not found in the response of ["+gen.getResponseId()+"].");
		
		Random rand = new Random();
		gen.updateGenerateValue(String.valueOf(Math.abs(rand.nextInt())));
		gen.sendRequest();
		input = gen.getGenerateValue();
		isFound = gen.getResponseId().split("-")[0].equalsIgnoreCase(input);
		Assert.assertEquals(isFound, Boolean.TRUE, "The generate value ["+input+"] was not found in the response of ["+gen.getResponseId()+"].");
	}
}
