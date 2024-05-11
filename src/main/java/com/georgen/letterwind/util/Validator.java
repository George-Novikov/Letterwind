package com.georgen.letterwind.util;

import java.util.Collection;

public class Validator {
    /* String validation */
    public static boolean isBlank(String value){
        return value.chars().allMatch(c -> c == 32);
    }

    public static boolean isValid(String value){
        return value != null && !value.isEmpty() && !isBlank(value);
    }

    /* Collections validation */
    public static <T> boolean isValid(Collection<T> collection){
        return collection != null && !collection.isEmpty();
    }
}
