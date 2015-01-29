/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.test.cukes;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.collections.Maps;

/**
 * Interceptor to change the order of the collection of tests.
 * 
 * @author Brian Becker
 */
public class CucumberInterceptor implements IMethodInterceptor {
    private final Map<Class, List<Object>> m_instances = Maps.newHashMap();

    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
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
        Collections.sort(methods, comparator);
        return methods;
    }
}