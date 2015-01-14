/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc.schema;

import com.orasi.utils.debug.JavaDebugBridge;
import com.orasi.utils.debug.WatchedInterpreter;
import static com.orasi.utils.debug.WatchedInterpreter.COLOR_BRIGHT_GREEN;
import static com.orasi.utils.debug.WatchedInterpreter.COLOR_BRIGHT_RED;
import static com.orasi.utils.debug.WatchedInterpreter.COLOR_BRIGHT_YELLOW;
import static com.orasi.utils.debug.WatchedInterpreter.COLOR_RESET;
import static com.google.common.base.Ascii.CR;
import java.lang.reflect.Method;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 *
 * @author brian.becker
 */
public class RestDebuggerTest {
    private Thread dt;
    private JavaDebugBridge debug;
    private boolean lastSuccess;
    protected boolean keepRunning = true;
    
    @BeforeClass
    public void setup() {
        debug = new JavaDebugBridge();
    }
    
    @AfterMethod(alwaysRun = true)
    public void checkFailure(ITestResult result) throws Exception {
        if (result.getStatus() == ITestResult.FAILURE) {
            debug.getInterpreter().error("TestNG Reporter: " + result.getName() + " failed");
            debug.debug();
            lastSuccess = false;
        } else {
            debug.getInterpreter().pass("TestNG Reporter: " + result.getName() + " passed");
            lastSuccess = true;
        }
    }
    
    @BeforeMethod(alwaysRun = true)
    public void startMessage(Method method) throws Exception {
        print(COLOR_BRIGHT_YELLOW + "TestNG Reporter: " + method.getName() + " starting" + COLOR_RESET + "\n");
    }
    
    @AfterClass
    public void keepRunning() {
        if(lastSuccess == true && keepRunning == true) {
            debug.debug();
        }
    }
    
    public void print(Object o) {
        this.debug.getInterpreter().print(o);
    }
    
    public String yesno(boolean yn) {
        if(yn == true)
            return COLOR_BRIGHT_GREEN + "YES" + COLOR_RESET;
        else
            return COLOR_BRIGHT_RED + "NO" + COLOR_RESET;
    }
    
    public void println(Object o) {
        this.print(o);
        this.print("\n");
    }
}
