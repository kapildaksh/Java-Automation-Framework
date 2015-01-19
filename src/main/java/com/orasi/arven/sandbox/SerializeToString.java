/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.arven.sandbox;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Collection;

class SerializeToString extends JsonSerializer<Object> {
    @Override
    public void serialize(Object t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        if(t instanceof Collection) {
            jg.writeStartArray();
            Collection c = (Collection)t;
            for(Object o : c) {
                jg.writeString(o.toString());
            }
            jg.writeEndArray();
        } else {
           jg.writeString(t.toString());
        }
    }
}