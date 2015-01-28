package com.orasi.utils.rest;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.model.CucumberFeature;
import gherkin.formatter.Formatter;
import java.util.LinkedList;
import java.util.List;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Factory;

/**
 * Cucumber TestNG Wrapper
 * Simply build a class following this general template, in the folder where
 * you have the feature files.:
 * 
 * @CucumberOptions(plugin = {"com.orasi.utils.test.format.ConsoleFormatter"}, tags = {"@conflict"})
 * public class MicroblogExampleCucumberTest extends CucumberNG {
 *   public MicroblogExampleCucumberTest() {
 *       super(MicroblogExampleCucumberTest.class);
 *   }
 * }
 */
public class CucumberNG {
    
    private final Formatter formatter;
    private final SharedReporter reporter;
    private final Runtime runtime;
    private final List<CucumberFeature> cucumberFeatures;
    
    /**
     * CucumberNG super constructor for test classes. The super constructor
     * should be called with the class of the location of the Step Definitions
     * and the Feature Files (in the resource directory).
     * 
     * @param clazz
     * @throws ClassNotFoundException 
     */
    public CucumberNG(Class clazz) throws ClassNotFoundException {
        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
        formatter = runtimeOptions.formatter(classLoader);
        reporter = new SharedReporter(runtimeOptions.reporter(classLoader));
        cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);
    }
    
    /**
     * Test Factory returns a complete TestNG Test class, annotated with a single
     * test. The test is generated from a feature file.
     * 
     * @return 
     */
    @Factory
    public Object[] cucumberTestData() {
        LinkedList<CucumberFeatureTest> tests = new LinkedList<CucumberFeatureTest>();
        for(CucumberFeature f : cucumberFeatures) {
            tests.add(new CucumberFeatureTest(f, formatter, reporter, runtime));
        }
        return (Object[]) tests.toArray();
    }
    
    @AfterSuite
    public void cucumberDone() {
        runtime.printSummary();
    }

}