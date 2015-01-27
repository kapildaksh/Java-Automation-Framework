package com.orasi.utils.rest;

import java.util.Map;

/**
 * The RestCollection is a warehouse of REST service calls. You should be able
 * to look up calls by ID or by name, the meaning of such which is defined by
 * the collection implementation itself. You may set a collection-wide
 * environment, which is usable for value substitution.
 * 
 * @author Brian Becker
 */
public interface RestCollection {
    public abstract RestRequest byId(String id);
    public abstract RestRequest byName(String name);
    public abstract RestCollection withEnv(Map variables);
    public abstract RestCollection withSession(RestSession session);
}
