package com.georgen.letterwind.api.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.georgen.letterwind.broker.handlers.EmptyErrorHandler;
import com.georgen.letterwind.broker.handlers.EmptySuccessHandler;
import com.georgen.letterwind.broker.handlers.ErrorHandler;
import com.georgen.letterwind.broker.handlers.SuccessHandler;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.broker.serializers.UniversalSerializer;
import com.georgen.letterwind.broker.validators.EmptyValidator;
import com.georgen.letterwind.broker.validators.MessageValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@JsonTypeInfo(use = JsonTypeInfo.Id.SIMPLE_NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JacksonAnnotationsInside
public @interface LetterwindMessage {
    Class<? extends MessageSerializer> serializer() default UniversalSerializer.class;
    Class<? extends MessageValidator> validator() default EmptyValidator.class;
    Class<? extends SuccessHandler> successHandler() default EmptySuccessHandler.class;
    Class<? extends ErrorHandler> errorHandler() default EmptyErrorHandler.class;
}
