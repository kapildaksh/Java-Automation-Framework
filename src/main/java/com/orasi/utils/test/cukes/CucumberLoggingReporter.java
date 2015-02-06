package com.orasi.utils.test.cukes;

import gherkin.formatter.Reporter;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;

/**
 * A Logging Reporter for Gherkin
 * This reporter holds the result, making it available in the current
 * scope. This is necessary for implementing test wrappers and test
 * engines which show pretty-printed reports alongside running TestNG
 * tests.
 * 
 * @author Brian Becker
 */
public class CucumberLoggingReporter implements Reporter {

    private Result resultValue;
    private final Reporter reporter;
    
    /**
     * Create a new logging reporter. This reporter will be accessed in
     * a state-changing fashion only when wrapped reporter commands are
     * called.
     * 
     * @param   r   Reporter to tie this reporter to
     */
    public CucumberLoggingReporter(Reporter r) {
        this.reporter = r;
    }
    
    @Override
    public void before(Match match, Result result) {
        this.reporter.before(match, result);	
    }

    @Override
    public void result(Result result) {
        this.resultValue = result;
        this.reporter.result(result);
    }
    
    /**
     * We can grab the last result of the reporter, which can be used to
     * launch a particular action by the TestNG Test: Passed, we can do
     * nothing. Skipped or Undefined, we can throw a Skipped Exception.
     * Failed, we can throw an error which we receive from this result.
     * 
     * @return      Last result which the reporter itself was given
     */
    public Result getLastResult() {
        return this.resultValue;
    }

    @Override
    public void after(Match match, Result result) {
        this.reporter.after(match, result);
    }

    @Override
    public void match(Match match) {
        this.reporter.match(match);
    }

    @Override
    public void embedding(String string, byte[] bytes) {
        this.reporter.embedding(string, bytes);
    }

    @Override
    public void write(String string) {
        this.reporter.write(string);
    }
    
}
