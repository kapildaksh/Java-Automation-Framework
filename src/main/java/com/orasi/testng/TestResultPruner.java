package com.orasi.testng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.orasi.utils.Log;

/**
 * This listener will help prune test results that have retry logic built into
 * the suite.  For instance, when a test re-runs we don't want to display the 
 * initial test that failed - instead only display the latest test that passed.
 * 
 * @see {@link RetryAnalyzer}
 * @author SonHuy.Pham@Disney.com
 */
public class TestResultPruner extends TestListenerAdapter {

    public TestResultPruner() {
        super();
    }

    /**
     * Will create a hash based on the test name, the method name, and lastly
     * if any parameters were passed to it (i.e. data-providers).
     * 
     * @see <a href="http://stackoverflow.com/questions/13131912/testng-retrying-failed-tests-doesnt-output-the-correct-test-results/18374673#18374673">TestNg Retry</a>
     * @param result
     * @return
     */
    private int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id = 31 * id + result.getMethod().getMethodName().hashCode();
        id = 31 * id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }

    @Override
    public void onFinish(ITestContext testContext) {

        super.onFinish(testContext);

        List<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();

        Set<Integer> passedTestIds = new HashSet<Integer>();
        Set<ITestResult> passedTests = testContext.getPassedTests().getAllResults();

        for (ITestResult passedTest : passedTests) {
            passedTestIds.add(getId(passedTest));
        }

        Set<Integer> failedTestIds = new HashSet<Integer>();
        Set<ITestResult> failedTests = testContext.getFailedTests().getAllResults();
        
        for (ITestResult failedTest : failedTests) {

            int failedTestId = getId(failedTest);

            if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {

                // If this test already exists, then we'll need to mark it for 
                // removal so that we counting it twice (or more).
                //
                // Notice that the passed-test list is untouched, we're only
                // editing the list of failed tests.

                testsToBeRemoved.add(failedTest);

            } else {
                failedTestIds.add(failedTestId);
            }
        }

        Iterator<ITestResult> iterator = failedTests.iterator();

        while (iterator.hasNext()) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                Log.getDefaultLogger().info("Pruning test result: " + testResult.toString());
                iterator.remove();
            }
        }
    }
}
