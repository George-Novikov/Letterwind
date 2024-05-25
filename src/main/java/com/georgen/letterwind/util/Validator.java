package com.georgen.letterwind.util;

import java.util.Collection;

public class Validator {
    public static boolean isBlank(String value){
        return value.chars().allMatch(c -> c == 32);
    }

    public static boolean isValid(String value){
        return value != null && !value.isEmpty() && !isBlank(value);
    }


}
