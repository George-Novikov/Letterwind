package com.georgen.letterwind.tools.extractors;

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
        Set<Method> annotatedMethods = Arrays
                .stream(methods)
                .filter(method -> Modifier.isPublic(method.getModifiers()) && method.isAnnotationPresent(LetterwindConsumer.class))
                .collect(Collectors.toSet());

        if (annotatedMethods != null && !annotatedMethods.isEmpty()){
            return extractFromAnnotatedMethods(annotatedMethods);
        } else {
            return extractFromPublicMethods(methods);
        }
    }

    private static Set<Class> extractFromAnnotatedMethods(Set<Method> annotatedMethods){
        Set<Class> types = new HashSet<>();

        for (Method method : annotatedMethods){
            Class[] methodTypes =  method.getParameterTypes();
            Set<Class> methodTypesSet = Arrays.stream(methodTypes).collect(Collectors.toSet());
            types.addAll(methodTypesSet);
        }

        return types;
    }

    private static Set<Class> extractFromPublicMethods(Method[] methods){
        Set<Method> publicMethods = Arrays
                .stream(methods)
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .collect(Collectors.toSet());

        Set<Class> types = new HashSet<>();

        for (Method method : publicMethods){
            Class[] methodTypes =  method.getParameterTypes();
            Set<Class> methodTypesSet = Arrays.stream(methodTypes).collect(Collectors.toSet());
            types.addAll(methodTypesSet);
        }

        return types;
    }
}
