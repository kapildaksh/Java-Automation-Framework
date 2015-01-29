/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.test.cukes;

import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import gherkin.formatter.Formatter;
import gherkin.formatter.model.ScenarioOutline;
import java.util.List;
import org.testng.ITest;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This is a Test Class which represents a Cucumber Scenario. It is intended
 * to be generated by a factory or another method which can convert Cucumber
 * features to TestNG tests.
 * 
 * @author Brian Becker
 */
public class CucumberScenarioTest implements ITest, CucumberMethod {

    public static int number = 0;
    private final CucumberScenario scenario;
    private final CucumberScenarioOutline outline;
    private final Formatter formatter;
    private final CucumberLoggingReporter reporter;
    private final Runtime runtime; 
    private final boolean firstScenario;
    private final int priority;
    private final CucumberFeature feature;
    private final boolean firstTestInFeature;
    
    private String testName;
    
    public CucumberScenarioTest(CucumberScenario cs, CucumberScenarioOutline cso, Formatter formatter,
            CucumberLoggingReporter reporter, Runtime runtime, boolean firstScenario, int priority, CucumberFeature feature, boolean firstTestInFeature) {
        this.scenario = cs;
        this.outline = cso;
        this.formatter = formatter;
        this.reporter = reporter;
        this.runtime = runtime;
        this.firstScenario = firstScenario;
        this.priority = priority;
        this.feature = feature;
        this.firstTestInFeature = firstTestInFeature;
    }    
    
    /**
     * The Cucumber test itself, which is executed, and a variety of exceptions
     * will be passed through Cucumber or generated if the test has been
     * determined to be skipped, or not defined.
     * 
     * @throws Throwable 
     */
    @Test(priority = 0)
    public void test() throws Throwable {
        if(firstScenario && outline != null) {
            outline.formatOutlineScenario(formatter);
            for(CucumberExamples exs : outline.getCucumberExamplesList()) {
                formatter.examples(exs.getExamples());
            }
        }
        if(firstTestInFeature && feature != null) {
            formatter.uri(feature.getPath());
            formatter.feature(feature.getGherkinFeature());
        }
        scenario.run(formatter, reporter, runtime);
        formatter.eof();
        switch(reporter.getLastResult().getStatus()) {
            case "failed":
                throw reporter.getLastResult().getError();
            case "skipped":
                throw new SkipException("Skipped Test");
            case "undefined":
                throw new SkipException("Undefined");
        }
        number++;
    }    

    ///**
    // * Before the method, get the test name to stuff into the current
    // * test name value.
    // * @param params
    // */
    @BeforeMethod(alwaysRun = true)
    public void before(Object[] params) {
        testName = scenario.getGherkinModel().getName();
        if(outline != null) {
            testName = testName + " [" + scenario.getVisualName().substring(1,scenario.getVisualName().length() - 1) + "]";
        }
    }
    
    /**
     * The Test Name which is generated by the Gherkin Keyword and Model
     * Name. It will be displayed in NetBeans and Eclipse test frontends,
     * and is needed to implement ITest.
     * 
     * @return test name
     */
    @Override
    public String getTestName() {
        return this.testName != null ? this.testName : "Unknown";
    }

    @Override
    public int priority() {
        return this.priority;
    }
    
}
