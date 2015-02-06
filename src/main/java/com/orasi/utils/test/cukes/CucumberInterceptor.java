package com.orasi.utils.test.cukes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

/**
 * Interceptor to change the order of the collection of tests. This is used
 * for a pretty printing view when the TestNG tests are also running in Maven
 * or an IDE.
 * 
 * @author Brian Becker
 */
public class CucumberInterceptor implements IMethodInterceptor {

    /**
     * Intercept the method instances as they are about to be passed to the
     * test runner. This uses the sort order of the methods according to their
     * priority value, in ascending order. This is essentially just a way of
     * forcing an order onto the test instances, to maintain the appearance
     * of a straightforward test. This interceptor can be removed or added
     * at will depending on the desire to have an asynchronous, fast test or
     * some pretty test logs.
     * 
     * @param   methods     Methods to be re-ordered
     * @param   context     Test Context
     * @return  List of Method Instances in desired order
     */
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        Comparator<IMethodInstance> comparator = new Comparator<IMethodInstance>() {
            private int getPriority(IMethodInstance mi) {
                Object inst = mi.getInstance();
                if(inst instanceof CucumberScenarioTest) {
                    CucumberScenarioTest ccm = (CucumberScenarioTest) inst;
                    return ccm.priority();
                }
                return 0;
            }

            @Override
            public int compare(IMethodInstance m1, IMethodInstance m2) {
                return getPriority(m1) - getPriority(m2);
            }
        };
        
        Collections.sort(methods, comparator);
        return methods;
    }

}
