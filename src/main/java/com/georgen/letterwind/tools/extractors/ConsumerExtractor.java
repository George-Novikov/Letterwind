package com.georgen.letterwind.tools.extractors;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsumerExtractor {

    public static boolean hasConsumingMethods(Class consumerClass){
        Method[] methods = consumerClass.getDeclaredMethods();

        return Arrays
                .stream(methods)
                .anyMatch(method ->
                        Modifier.isPublic(method.getModifiers())
                        && method.isAnnotationPresent(LetterwindConsumer.class));
    }


    public static Set<Method> extractConsumingMethods(Class consumerClass){
        Method[] methods = consumerClass.getDeclaredMethods();

        return Arrays
                .stream(methods)
                .filter(method ->
                        Modifier.isPublic(method.getModifiers())
                        && method.isAnnotationPresent(LetterwindConsumer.class))
                .collect(Collectors.toSet());
    }
}
