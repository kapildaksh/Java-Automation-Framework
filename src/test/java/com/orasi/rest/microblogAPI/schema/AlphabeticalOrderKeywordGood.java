/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.microblogAPI.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.processing.Processor;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.keyword.validator.AbstractKeywordValidator;
import com.github.fge.jsonschema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 *
 * @author brian.becker
 */
public class AlphabeticalOrderKeywordGood extends AbstractKeywordValidator {

    public AlphabeticalOrderKeywordGood(JsonNode n) {
        super("x-in-alphabetical-order");
    }
    
    @Override
    public String toString() {
        return "cannotdisplay";
    }

    public void validate(Processor<FullData, FullData> prcsr, ProcessingReport pr, MessageBundle mb, FullData fd) throws ProcessingException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
