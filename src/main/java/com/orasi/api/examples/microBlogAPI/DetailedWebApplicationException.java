/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import java.io.PrintStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.commons.io.output.ByteArrayOutputStream;

@Provider
public class DetailedWebApplicationException implements ExceptionMapper<WebApplicationException> {

    public static class FaultMessage {
        private final String message, exception;
        
        public FaultMessage(String message, String exception) {
            this.message = message;
            this.exception = exception;
        }
        
        public String getMessage() { return this.message; }
        public String getException() { return this.exception; }
    }
    
    @Override
    public Response toResponse(final WebApplicationException exception) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(os);
        exception.printStackTrace(ps);
        return Response.status(exception.getResponse().getStatus()).type("text/plain").entity(exception.getMessage() + "\n\n" + os.toString()).build();
    }

}