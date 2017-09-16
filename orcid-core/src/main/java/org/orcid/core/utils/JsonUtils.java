/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.core.utils;

import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Will Simpson
 * 
 */
public class JsonUtils {

    static ObjectMapper mapper = new ObjectMapper(); // thread safe!
    static ObjectMapper mapperFromJSON = new ObjectMapper(); // thread safe!
    static {
        mapperFromJSON.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    public static String convertToJsonString(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readObjectFromJsonString(String jsonString, Class<T> clazz) {
        try {
            return mapperFromJSON.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JSONObject extractObject(JSONObject parent, String key) {
        if (parent.isNull(key)) {
            return null;
        }
        try {
            return parent.getJSONObject(key);
        } catch (JSONException e) {
            throw new RuntimeException("Error extracting json object", e);
        }
    }

    public static String extractString(JSONObject record, String key) {
        if (record.isNull(key)) {
            return null;
        }
        try {
            return record.getString(key);
        } catch (JSONException e) {
            throw new RuntimeException("Error extracting string from json", e);
        }
    }

    public static int extractInt(JSONObject record, String key) {
        if (record.isNull(key)) {
            return -1;
        }
        try {
            return record.getInt(key);
        } catch (JSONException e) {
            throw new RuntimeException("Error extracting int from json", e);
        }
    }

}
