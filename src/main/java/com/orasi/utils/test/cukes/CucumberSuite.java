package com.orasi.utils.test.cukes;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import cucumber.runtime.model.CucumberTagStatement;
import gherkin.formatter.Formatter;
import java.util.LinkedList;
import java.util.List;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Factory;
import org.testng.annotations.Listeners;

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
@Listeners({CucumberInterceptor.class})
public class CucumberSuite {
    
    private final Formatter formatter;
    private final CucumberLoggingReporter reporter;
    private final Runtime runtime;
    private final List<CucumberFeature> cucumberFeatures;
    
    /**
     * CucumberNG super constructor for test classes. The super constructor
     * should be called with the class of the location of the Step Definitions
     * and the Feature Files (in the resource directory).
     * 
     * @param   clazz       Class used for ClassLoader for Cucumber Resources
     * @throws  ClassNotFoundException 
     */
    public CucumberSuite(Class clazz) throws ClassNotFoundException {
        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
        formatter = runtimeOptions.formatter(classLoader);
        reporter = new CucumberLoggingReporter(runtimeOptions.reporter(classLoader));
        cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);
    }
    
    /**
     * Test Factory returns a complete TestNG Test class, annotated with a single
     * test. The test is generated from a feature file.
     * 
     * @return  Factory for generating TestNG Test classes
     * @throws  java.lang.Exception 
     */    
    @Factory
    public Object[] cucumberFactory() throws Exception {
        int priority = 0;
        LinkedList<CucumberScenarioTest> tests = new LinkedList<CucumberScenarioTest>();
        for(CucumberFeature f : cucumberFeatures) {
            boolean firstTestInFeature = true;
            for (CucumberTagStatement ts : f.getFeatureElements()) {
                if(ts instanceof CucumberScenarioOutline) {
                    CucumberScenarioOutline outline = (CucumberScenarioOutline) ts;
                    boolean firstScenario = true;
                    for (CucumberExamples examples : outline.getCucumberExamplesList()) {
                        for (CucumberScenario cs : examples.createExampleScenarios()) {
                            CucumberScenarioTest test = new CucumberScenarioTest(cs, firstScenario ? outline : null, formatter, reporter, runtime, priority++, firstTestInFeature ? f : null);                           
                            tests.add(test);
                            firstScenario = false;
                        }
                    }
                }
                if(ts instanceof CucumberScenario) {
                    tests.add(new CucumberScenarioTest((CucumberScenario) ts, null, formatter, reporter, runtime, priority++, firstTestInFeature ? f : null));
                }
                firstTestInFeature = false;
            }
        }
        return tests.toArray();
    }
    
    /**
     * We want to close the formatter and runtime cleanly, as well as
     * print a summary of the Cucumber tasks we have completed. The
     * TestNG results are not as fine-grained.
     */
    @AfterSuite
    public void cucumberDone() {
        formatter.done();
        formatter.close();
        runtime.printSummary();
    }

}