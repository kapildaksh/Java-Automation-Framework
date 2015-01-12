/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest.schema;

import com.github.fge.jackson.NodeType;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.format.AbstractFormatAttribute;
import com.github.fge.jsonschema.format.FormatAttribute;
import com.github.fge.jsonschema.processors.data.FullData;
import com.github.fge.msgsimple.bundle.MessageBundle;

/**
 *
 * @author brian.becker
 */
public class PathAttributeGood extends AbstractFormatAttribute {
    private static final FormatAttribute INSTANCE = new PathAttributeGood();

    private PathAttributeGood()
    {
        super("device-path", NodeType.STRING);
    }

    public static FormatAttribute getInstance()
    {
        return INSTANCE;
    }

    public void validate(ProcessingReport pr, MessageBundle mb, FullData fd) throws ProcessingException {
        final String value = fd.getInstance().getNode().textValue();
        System.out.println(value);
        
        // Device name does not contain dev path
        if(!value.contains("/dev/")) {
            pr.error(newMsg(fd, mb, "invalidPath").put("input", value));
        }
    }
    
}
