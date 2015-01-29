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
import gherkin.formatter.model.Tag;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
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
     * @param clazz
     * @throws ClassNotFoundException 
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
     * @return 
     * @throws java.lang.Exception 
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
                            CucumberScenarioTest test = new CucumberScenarioTest(cs, outline, formatter, reporter, runtime, firstScenario, priority++, f, firstTestInFeature);                           
                            tests.add(test);
                            firstScenario = false;
                        }
                    }
                }
                if(ts instanceof CucumberScenario) {
                    tests.add(new CucumberScenarioTest((CucumberScenario) ts, null, formatter, reporter, runtime, false, priority++, f, firstTestInFeature));
                }
                firstTestInFeature = false;
            }
        }
        return tests.toArray();
    }
    
    /**
     * The Tag Statement Matcher is used to determine if a given tagged item
     * is matched by any of the listed tags.
     * 
     * @param ts
     * @param tags
     * @return 
     */
    public static boolean tagStatementMatchesTags(CucumberTagStatement ts, List<String> tags) {
        List<Tag> other = ts.getGherkinModel().getTags();
        for(Tag t : other) {
            if(tags.contains(t.getName())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * The Tag Enabled version of the Factory. With a selected group of tags,
     * the CucumberInterceptor should be called which will rewrite the method
     * list.
     * 
     * @param tags
     * @param notags
     * @return 
     * @throws java.lang.Exception 
     */
    public Object[] cucumberGroupFactory(List<String> tags, List<String> notags) throws Exception {
        int priority = 0;
        LinkedList<CucumberScenarioTest> tests = new LinkedList<CucumberScenarioTest>();
        for(CucumberFeature f : cucumberFeatures) {
            boolean firstTestInFeature = true;
            for (CucumberTagStatement ts : f.getFeatureElements()) {
                if(ts instanceof CucumberScenarioOutline) {
                    CucumberScenarioOutline outline = (CucumberScenarioOutline) ts;
                    if((tagStatementMatchesTags(ts, tags) && !tagStatementMatchesTags(ts, notags)) || (!tagStatementMatchesTags(ts, notags) && tags.isEmpty()) || (tags.isEmpty() && notags.isEmpty())) {
                        boolean firstScenario = true;
                        for (CucumberExamples examples : outline.getCucumberExamplesList()) {
                            for (CucumberScenario cs : examples.createExampleScenarios()) {
                                CucumberScenarioTest test = new CucumberScenarioTest(cs, outline, formatter, reporter, runtime, firstScenario, priority++, f, firstTestInFeature);
                                cs.getGherkinModel().getTags();
                                tests.add(test);
                                firstScenario = false;
                            }
                        }
                    }
                }
                if(ts instanceof CucumberScenario) {
                    if((tagStatementMatchesTags(ts, tags) && !tagStatementMatchesTags(ts, notags)) || (!tagStatementMatchesTags(ts, notags) && tags.isEmpty()) || (tags.isEmpty() && notags.isEmpty())) {
                        tests.add(new CucumberScenarioTest((CucumberScenario) ts, null, formatter, reporter, runtime, false, priority++, f, firstTestInFeature));
                    }
                }
                firstTestInFeature = false;
            }
        }
        return tests.toArray();
    }    
    
    @BeforeSuite
    public void cucumberStart(ITestContext suite) {
    }
    
    @AfterSuite
    public void cucumberDone() {
        formatter.done();
        formatter.close();
        runtime.printSummary();
        System.out.println(CucumberScenarioTest.number);
    }

}