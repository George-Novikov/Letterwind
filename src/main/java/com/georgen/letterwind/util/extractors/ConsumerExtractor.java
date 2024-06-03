package com.georgen.letterwind.util.extractors;

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

    public static Set<Method> extractConsumingMethods(Class consumerClass, Class messageType){
        Method[] methods = consumerClass.getDeclaredMethods();

        return Arrays
                .stream(methods)
                .filter(method ->
                        isPublic(method)
                                && isConsumer(method)
                                && hasParameterType(method, messageType))
                .collect(Collectors.toSet());
    }

    private static boolean isPublic(Method method){
        return Modifier.isPublic(method.getModifiers());
    }

    private static boolean isConsumer(Method method){
        return method.isAnnotationPresent(LetterwindConsumer.class);
    }

    private static boolean hasParameterType(Method method, Class parameterType){
        return Arrays.stream(method.getParameterTypes()).anyMatch(type -> type.equals(parameterType));
    }
}
