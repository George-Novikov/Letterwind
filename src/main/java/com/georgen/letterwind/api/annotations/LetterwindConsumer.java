package com.georgen.letterwind.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.TYPE_USE})
public @interface LetterwindConsumer {
    int concurrentCopies() default 0;
    boolean isPreActivated() default false;
    boolean isCrossTopic() default false;
}
