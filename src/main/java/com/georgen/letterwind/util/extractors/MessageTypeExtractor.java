package com.georgen.letterwind.util.extractors;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageTypeExtractor {
    public static Set<Class> extract(Class consumerClass){
        Method[] methods = consumerClass.getDeclaredMethods();
        Set<Method> annotatedMethods = Arrays.stream(methods)
                .filter(MessageTypeExtractor::isValidMethod)
                .collect(Collectors.toSet());

        if (annotatedMethods != null && !annotatedMethods.isEmpty()){
            return extractFromAnnotatedMethods(annotatedMethods);
        } else {
            return extractFromPublicMethods(methods);
        }
    }

    private static boolean isValidMethod(Method method){
        return Modifier.isPublic(method.getModifiers()) && method.isAnnotationPresent(LetterwindConsumer.class);
    }

    private static Set<Class> extractFromAnnotatedMethods(Set<Method> annotatedMethods){
        return annotatedMethods.stream()
                .map(Method::getParameterTypes)
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
    }

    private static Set<Class> extractFromPublicMethods(Method[] methods){
        return Arrays.stream(methods)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(Method::getParameterTypes)
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
    }
}
