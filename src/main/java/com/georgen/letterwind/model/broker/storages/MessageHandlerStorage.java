package com.georgen.letterwind.model.broker.storages;

import com.georgen.letterwind.api.annotations.LetterwindMessage;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.broker.validators.MessageValidator;
import com.georgen.letterwind.model.constants.MessageFlowEvent;

import java.util.concurrent.ConcurrentHashMap;

public class MessageHandlerStorage {
    private ConcurrentHashMap<Class, Class<? extends MessageSerializer>> serializers;
    private ConcurrentHashMap<Class, Class<? extends MessageValidator>> validators;
    private ConcurrentHashMap<Class, Class<? extends SuccessHandler>> successHandlers;
    private ConcurrentHashMap<Class, Class<? extends ErrorHandler>> errorHandlers;

    private MessageHandlerStorage(){
        this.serializers = new ConcurrentHashMap<>();
        this.validators = new ConcurrentHashMap<>();
        this.successHandlers = new ConcurrentHashMap<>();
        this.errorHandlers = new ConcurrentHashMap<>();
    }

    public void register(Class messageType) {
        if (String.class.equals(messageType)) return;

        Class<? extends MessageSerializer> serializerClass = extractSerializer(messageType);
        if (serializerClass != null) serializers.put(messageType, serializerClass);

        Class<? extends MessageValidator> validatorClass = extractValidator(messageType);
        if (validatorClass != null) validators.put(messageType, validatorClass);

        Class<? extends SuccessHandler> successHandlerClass = extractSuccessHandler(messageType);
        if (SuccessHandler.isValid(successHandlerClass)) successHandlers.put(messageType, successHandlerClass);

        Class<? extends ErrorHandler> errorHandlerClass = extractErrorHandler(messageType);
        if (ErrorHandler.isValid(errorHandlerClass)) errorHandlers.put(messageType, errorHandlerClass);
    }

    public Class<? extends MessageSerializer> getSerializer(Class messageType){
        return this.serializers.get(messageType);
    }

    public Class<? extends MessageValidator> getValidator(Class messageType){
        return this.validators.get(messageType);
    }

    public Class<? extends ErrorHandler> getErrorHandler(Class messageType){
        return this.errorHandlers.get(messageType);
    }

    public Class<? extends SuccessHandler> getSuccessHandler(Class messageType){
        return this.successHandlers.get(messageType);
    }

    private Class<? extends MessageSerializer> extractSerializer(Class messageType) {
        LetterwindMessage letterwindMessage = (LetterwindMessage) messageType.getAnnotation(LetterwindMessage.class);
        return letterwindMessage.serializer();
    }

    public Class<? extends MessageValidator> extractValidator(Class messageType) {
        LetterwindMessage letterwindMessage = (LetterwindMessage) messageType.getAnnotation(LetterwindMessage.class);
        return letterwindMessage.validator();
    }

    public Class<? extends SuccessHandler> extractSuccessHandler(Class messageType) {
        LetterwindMessage letterwindMessage = (LetterwindMessage) messageType.getAnnotation(LetterwindMessage.class);
        return letterwindMessage.successHandler();
    }

    public Class<? extends ErrorHandler> extractErrorHandler(Class messageType) {
        LetterwindMessage letterwindMessage = (LetterwindMessage) messageType.getAnnotation(LetterwindMessage.class);
        return letterwindMessage.errorHandler();
    }

    public boolean hasFinalEventHandlers(Class messageType, MessageFlowEvent event){
        switch (event){
            case SUCCESS:
                return successHandlers.get(messageType) != null;
            case ERROR:
                return errorHandlers.get(messageType) != null;
            default:
                return false;
        }
    }



    private static class InstanceHolder {
        private static final MessageHandlerStorage INSTANCE = new MessageHandlerStorage();
    }

    public static MessageHandlerStorage getInstance(){
        return InstanceHolder.INSTANCE;
    }
}
