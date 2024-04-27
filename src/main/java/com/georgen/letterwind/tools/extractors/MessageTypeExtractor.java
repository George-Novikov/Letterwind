package com.georgen.letterwind.tools.extractors;

import com.georgen.letterwind.api.annotations.ConsumingMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageTypeExtractor {
    public static Set<Class> extract(Class consumerClass){
        Method[] methods = consumerClass.getDeclaredMethods();
        Set<Method> annotatedMethods = Arrays
                .stream(methods)
                .filter(method -> method.isAnnotationPresent(ConsumingMethod.class))
                .collect(Collectors.toSet());

        if (annotatedMethods != null && !annotatedMethods.isEmpty()){
            return extractFromAnnotatedMethods(annotatedMethods);
        } else {
            return extractFromPublicMethods(methods);
        }
    }

    private static Set<Class> extractFromAnnotatedMethods(Set<Method> annotatedMethods){
        return annotatedMethods
                .stream()
                .map(method -> method.getReturnType())
                .collect(Collectors.toSet());
    }

    private static Set<Class> extractFromPublicMethods(Method[] methods){
        return Arrays
                .stream(methods)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(method -> method.getReturnType())
                .collect(Collectors.toSet());
    }
}
