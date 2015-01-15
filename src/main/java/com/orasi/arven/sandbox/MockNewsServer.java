package com.orasi.arven.sandbox;

import sparkfive.Request;
import sparkfive.Response;
import sparkfive.Route;
import static sparkfive.Spark.*;

public class MockNewsServer {
    public static void main( final String[] args ) throws Exception {
        get("/hello", new Route() {
            @Override
            public Object handle(Request req, Response res) {
                return "Hello Plain Text";
            }
        });
    }
}