package com.georgen.letterwind.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Serializer {

    public static String toJson(Object object) throws JsonProcessingException {
        return getInstance().writeValueAsString(object);
    }

    public static <T> T deserialize(String json, Class javaClass) throws JsonProcessingException {
        return (T) getInstance().readValue(json, javaClass);
    }

    public static <T> T deserialize(String json, TypeReference type) throws JsonProcessingException {
        return (T) getInstance().readValue(json, type);
    }

    private static class SerializerHolder {
        private static final ObjectMapper INSTANCE = new ObjectMapper();
        private static boolean isInit;

        private static void init(){
            SerializerHolder.INSTANCE.findAndRegisterModules();
            isInit = true;
        }

        private static boolean isInit(){
            return isInit;
        }
    }

    public static ObjectMapper getInstance(){
        if (!SerializerHolder.isInit()) SerializerHolder.init();
        return SerializerHolder.INSTANCE;
    }
}
