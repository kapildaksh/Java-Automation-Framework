/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.test.cukes;

import gherkin.formatter.Reporter;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;

/**
 * A Logging Reporter for Gherkin
 * This reporter holds the result, making it available in the current
 * scope. This is necessary for implementing test wrappers, etc.
 * 
 * @author Brian Becker
 */
public class CucumberLoggingReporter implements Reporter {

    private Result resultValue;
    private final Reporter reporter;
    
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
