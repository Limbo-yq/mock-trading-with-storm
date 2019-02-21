package com.limbo.actual.mocktrading.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JackJsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <C> C toBean(String json, Class<C> c){
        try {
            return mapper.readValue(json,c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
