package com.orasi.testng;

import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.orasi.utils.Log;
import com.orasi.utils.Utils;

/**
 * TODO: We can further improve/expand this by detecting how fatal the 
 * failing-error was.  This can possibly save time by only retrying with  
 * well-known and intermittent issues.
 * <p>
 * This is typically be used in conjunction with a test-result-pruner.
 * 
 * @see {@link HtmlGenerator}
 * @author SonHuy.Pham@Disney.com
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    int retryAttempts = 0;

    public RetryAnalyzer() {
    }

    @Override
    public boolean retry(ITestResult result) {
        
        // If the test is successful then we can go ahead and skip the retry.
        
        if(result == null || result.isSuccess()) {
            return false;
        }
        
        ITestContext testContext = result.getTestContext();
        RetryConfiguration config = RetryConfiguration.getInstance();
        
        String testName = (result.getMethod() != null) ? 
                                result.getMethod().getMethodName() : 
                                result.getName();
        
        Integer number = 
                (testContext != null && testName != null) ?
                        config.lookupTestRetry(testName) : null;
        
        int maxAttempts = (number == null) ? 
                config.getMaxRetries() : number.intValue();
                
        if(++retryAttempts > maxAttempts) {
            return false;
        }
        
        config.incrementRetryStatus(testName);
        
        Log.getDefaultLogger().info("Retrying " + result.getName() + " [" + retryAttempts + "/" + maxAttempts + "]");
        
        // This might not be needed - Anyway, we're going to let other threads
        // catch up before we kick off another iteration.
        Utils.sleep(1500);
        
        return true;
    }
}
