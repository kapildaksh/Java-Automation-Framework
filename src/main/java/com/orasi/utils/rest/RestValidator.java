package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.okhttp.Response;
import java.util.List;

/**
 *
 * @author 
 */
public interface RestValidator {
    public abstract JsonNode validate(List<Patch> ignores, List<Patch> patches, Response res) throws Exception;
}
