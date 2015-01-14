/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.debug;

import bsh.Interpreter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brian Becker
 */
public class JavaDebugBridge implements Runnable {
    
    private Process telnet;
    private WatchedInterpreter watched;
    
    public JavaDebugBridge() {
        ServerSocket listener;
        Socket socket;
        try {
            telnet = Runtime.getRuntime().exec("bin/puttytel -load javadebug");
            listener = new ServerSocket(9090, 0, InetAddress.getLoopbackAddress());
            socket = listener.accept();
            Interpreter bshi = new Interpreter();
            watched = new WatchedInterpreter(bshi, socket.getInputStream(), socket.getOutputStream());
        } catch (Exception ex) {
            Logger.getLogger(JavaDebugBridge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Runs the server.
     */
    public void run() {
        while(!watched.isClosing()) {
            watched.run();
        }
    }
    
    public void debug() {
        try {
            watched.setClosing(false);
            Thread t = new Thread(this);
            t.start();
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(JavaDebugBridge.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void destroy() {
        telnet.destroy();
    }
    
    public WatchedInterpreter getInterpreter() {
        return this.watched;
    }
    
}
