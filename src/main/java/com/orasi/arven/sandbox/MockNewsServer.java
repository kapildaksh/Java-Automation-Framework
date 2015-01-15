package com.orasi.arven.sandbox;

import spark.Request;
import spark.Response;
import spark.Route;

public class MockNewsServer {
    public static void main( final String[] args ) throws Exception {
            spark.Spark.get(new Route("/hello") {

                @Override
                public Object handle(Request rqst, Response rspns) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            
            });
        //( x, y ) -> x + y;
        //spark.Spark.get("/hello", new spark.Route() {

       //     @Override
       //     public Object handle(Request rqst, Response rspns) throws Exception {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //    }
            
       // });
    }
}