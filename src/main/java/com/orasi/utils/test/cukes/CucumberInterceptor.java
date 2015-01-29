/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.test.cukes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.testng.IClass;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.collections.Maps;
import org.testng.internal.ClassInfoMap;
import org.testng.internal.Configuration;
import org.testng.internal.MethodInstance;
import org.testng.internal.RunInfo;
import org.testng.internal.TestNGClassFinder;
import org.testng.internal.TestNGMethod;
import org.testng.internal.TestNGMethodFinder;
import org.testng.xml.XmlClass;

public class CucumberInterceptor implements IMethodInterceptor {
    private final Map<Class, List<Object>> m_instances = Maps.newHashMap();

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        List<IMethodInstance> li = new LinkedList<IMethodInstance>();
        XmlClass cls = context.getCurrentXmlTest().getClasses().get(0);
        try {
            CucumberSuite suite = (CucumberSuite) cls.getSupportClass().getConstructor().newInstance();
            RunInfo info = new RunInfo();
            TestNGMethodFinder tngmf = new TestNGMethodFinder(info, context.getSuite().getAnnotationFinder());
            TestNGClassFinder cf = new TestNGClassFinder(new ClassInfoMap(context.getCurrentXmlTest().getClasses()), m_instances, context.getCurrentXmlTest(), new Configuration(), context);
            IClass clss = cf.getIClass(CucumberScenarioTest.class);

            List<String> tags = new LinkedList<String>();
            for(String tag : context.getCurrentXmlTest().getIncludedGroups()) {
                tags.add("@" + tag);
            }
            List<String> notags = new LinkedList<String>();
            for(String tag : context.getCurrentXmlTest().getExcludedGroups()) {
                notags.add("@" + tag);
            }

            Object[] tests = suite.cucumberFactory(); //suite.cucumberGroupFactory(tags, notags);
            for(Object o : tests) {
                if(o instanceof CucumberScenarioTest) {
                    // Reference to our actual test instance, if only this was accepted by the intercept
                    final CucumberScenarioTest test = (CucumberScenarioTest) o;
                    // Build a test method, a method instance wrapper. The method must be called "test" with no parameters
                    TestNGMethod ngm = new TestNGMethod(CucumberScenarioTest.class.getMethod("test"), context.getSuite().getAnnotationFinder(), context.getCurrentXmlTest(), o);
                    MethodInstance mi = new MethodInstance(ngm);
                    // Build a class implementation, which contains the context as well as the actual class.
                    // Here we work around the lack of a public interface
                    CucumberTestClass tc = new CucumberTestClass(clss, tngmf, context.getSuite().getAnnotationFinder(), info, context.getCurrentXmlTest(), context.getCurrentXmlTest().getClasses().get(0));
                    mi.getMethod().setTestClass(tc);
                    // Fix the methods with class, huge array of all methods in the class
                    for(ITestNGMethod[] sets : Arrays.asList(tc.getAfterClassMethods(), tc.getAfterGroupsMethods(), tc.getAfterSuiteMethods(), tc.getAfterTestConfigurationMethods(), tc.getAfterTestMethods(), tc.getAfterTestMethods(), tc.getBeforeClassMethods(), tc.getBeforeGroupsMethods(), tc.getBeforeSuiteMethods(), tc.getBeforeTestConfigurationMethods(), tc.getBeforeTestMethods())) {
                        // Fix each method in each set
                        for(ITestNGMethod m : sets) {
                            m.setTestClass(tc);
                        }  
                    }
                    li.add(mi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        Comparator<IMethodInstance> comparator = new Comparator<IMethodInstance>() {
            private int getPriority(IMethodInstance mi) {
                Object inst = mi.getInstance();
                if(inst instanceof CucumberMethod) {
                    CucumberMethod ccm = (CucumberMethod) inst;
                    return ccm.priority();
                }
                return 0;
            }

            @Override
            public int compare(IMethodInstance m1, IMethodInstance m2) {
                return getPriority(m1) - getPriority(m2);
            }
        };
//        List<IMethodInstance> li = methods;
        Collections.sort(li, comparator);
        return li;
    }
}