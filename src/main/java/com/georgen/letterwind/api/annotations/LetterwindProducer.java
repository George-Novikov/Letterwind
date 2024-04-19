package com.georgen.letterwind.api.annotations;

import com.georgen.letterwind.api.serializers.MessageSerializer;
import com.georgen.letterwind.api.serializers.SimpleSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
public @interface LetterwindProducer {
    Class<? extends MessageSerializer> serializer() default SimpleSerializer.class;
}
