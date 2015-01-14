/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc.schema;

import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.keyword.syntax.checkers.AbstractSyntaxChecker;
import com.github.fge.jsonschema.core.keyword.syntax.checkers.SyntaxChecker;
import com.github.fge.jsonschema.core.tree.SchemaTree;
import com.github.fge.jsonschema.core.util.RhinoHelper;
import com.github.fge.msgsimple.bundle.MessageBundle;

import java.util.Collection;

/**
 * Syntax checker for the {@code pattern} keyword
 *
 * @see RhinoHelper
 */
public final class AlphabeticalOrderSyntaxChecker extends AbstractSyntaxChecker
{
    private static final SyntaxChecker INSTANCE = new AlphabeticalOrderSyntaxChecker();

    public static SyntaxChecker getInstance()
    {
        return INSTANCE;
    }

    private AlphabeticalOrderSyntaxChecker ()
    {
        super("x-in-alphabetical-order", NodeType.STRING);
    }

    @Override
    protected void checkValue(final Collection<JsonPointer> pointers,
        final MessageBundle bundle, final ProcessingReport report,
        final SchemaTree tree)
        throws ProcessingException
    {
        final String value = getNode(tree).textValue();

        if (!RhinoHelper.regexIsValid(value))
            report.error(newMsg(tree, bundle, "common.invalidRegex")
                .putArgument("value", value));
    }
}
