package com.georgen.letterwind.model.broker.storages;

import com.georgen.letterwind.api.annotations.LetterwindConsumer;
import com.georgen.letterwind.model.exceptions.LetterwindException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ConsumerMethodStorage {
    private ConcurrentHashMap<Class, Set<Method>> consumingMethods;

    private ConsumerMethodStorage(){
        consumingMethods = new ConcurrentHashMap<Class, Set<Method>>();
    }

    public void register(Class consumerClass) throws LetterwindException {
        Set<Method> methods = extractConsumingMethods(consumerClass);
        if (methods == null || methods.isEmpty()) throwNoConsumerMethods(consumerClass);
        consumingMethods.put(consumerClass, methods);
    }

    public Set<Method> getForConsumerMessageType(Class consumerClass, Class messageType) throws LetterwindException {
        Set<Method> methods = consumingMethods.get(consumerClass);
        if (methods == null) throwNotRegisteredConsumer(consumerClass);

        methods = getForMessageType(methods, messageType);
        if (methods.isEmpty()) throwNoMethodsForMessageType(consumerClass, messageType);

        return methods;
    }

    private Set<Method> getForMessageType(Set<Method> methods, Class messageType){
        return methods.stream()
                .filter(method -> hasParameterType(method, messageType))
                .collect(Collectors.toSet());
    }

    private Set<Method> extractConsumingMethods(Class consumerClass){
        Method[] methods = consumerClass.getDeclaredMethods();
        return Arrays
                .stream(methods)
                .filter(method -> isValid(method))
                .collect(Collectors.toSet());
    }

    private boolean isValid(Method method){
        return isPublic(method)
                && isConsumer(method);
    }

    private boolean isPublic(Method method){
        return Modifier.isPublic(method.getModifiers());
    }

    private boolean isConsumer(Method method){
        return method.isAnnotationPresent(LetterwindConsumer.class);
    }

    private boolean hasParameterType(Method method, Class parameterType){
        return Arrays.stream(method.getParameterTypes()).anyMatch(type -> type.equals(parameterType));
    }

    private void throwNoConsumerMethods(Class consumerClass) throws LetterwindException {
        throw new LetterwindException(
                String.format(
                        "To be added to a topic, the %s class must have methods marked with the @LetterwindConsumer annotation",
                        consumerClass.getSimpleName()
                )
        );
    }

    private void throwNotRegisteredConsumer(Class consumerClass) throws LetterwindException {
        throw new LetterwindException(
                String.format(
                        "%s class is not a registered consumer or has no @LetterwindConsumer methods",
                        consumerClass.getSimpleName()
                )
        );
    }

    private void throwNoMethodsForMessageType(Class consumerClass, Class messageType) throws LetterwindException {
        throw new LetterwindException(
                String.format(
                        "%s class has no methods with the %s argument type.",
                        consumerClass.getSimpleName(),
                        messageType.getSimpleName()
                )
        );
    }

    private static class InstanceHolder {
        private static final ConsumerMethodStorage INSTANCE = new ConsumerMethodStorage();
    }

    public static ConsumerMethodStorage getInstance(){
        return InstanceHolder.INSTANCE;
    }

}
