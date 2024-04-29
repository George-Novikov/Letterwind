package com.georgen.letterwind.tools.extractors;

import com.georgen.letterwind.api.annotations.ConsumingMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConsumerExtractor {
    public static Set<Method> extractConsumingMethods(Class consumerClass){
        Method[] methods = consumerClass.getDeclaredMethods();

        Set<Method> publicMethods = Arrays
                .stream(methods)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .collect(Collectors.toSet());

        Set<Method> annotatedMethods = publicMethods
                .stream()
                .filter(method -> method.isAnnotationPresent(ConsumingMethod.class))
                .collect(Collectors.toSet());

        return annotatedMethods.isEmpty() ? publicMethods : annotatedMethods;
    }
}
