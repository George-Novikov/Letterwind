package com.georgen.letterwind.api.annotations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.georgen.letterwind.broker.serializers.MessageSerializer;
import com.georgen.letterwind.broker.serializers.LocalUniversalSerializer;
import com.georgen.letterwind.broker.validators.EmptyValidator;
import com.georgen.letterwind.broker.validators.MessageValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JacksonAnnotationsInside
public @interface LetterwindMessage {
    Class<? extends MessageSerializer> serializer() default LocalUniversalSerializer.class;
    Class<? extends MessageValidator> validator() default EmptyValidator.class;
}
